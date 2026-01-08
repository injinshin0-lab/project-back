package kr.co.kosmo.project_back.user.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.user.dto.SignUpDto;
import kr.co.kosmo.project_back.user.dto.LoginDto;
import kr.co.kosmo.project_back.user.dto.EmailCodeDto;
import kr.co.kosmo.project_back.user.dto.AddressDto;
import kr.co.kosmo.project_back.user.dto.UserResponseDto;
import kr.co.kosmo.project_back.user.mapper.LoginMapper;
import kr.co.kosmo.project_back.user.mapper.SelectedCategoryMapper;
import kr.co.kosmo.project_back.admin.mapper.AdminCategoryMapper;
import kr.co.kosmo.project_back.admin.dto.CategoryDto;
import kr.co.kosmo.project_back.user.dto.FindUserIdRequestDto;
import kr.co.kosmo.project_back.user.dto.FindPasswordRequestDto;
import kr.co.kosmo.project_back.user.dto.LoginRequestDto;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private final LoginMapper loginMapper;
    private final SelectedCategoryMapper userSelectedCategoryMapper;
    private final AdminCategoryMapper categoryMapper;
    private final HttpSession session;

    public UserResponseDto getAutoLoginToken(String token) {
        return loginMapper.findByAutoLoginToken(token);
    }

    public UserResponseDto login(LoginDto dto) {
        UserResponseDto user = loginMapper.findByLoginId(dto.getLoginId());
        if (user == null) {
            return null;
        }
        // 평문 비밀번호 직접 비교
        if (user.getPassword() != null && user.getPassword().equals(dto.getPassword())) {
            return user;
        }
        return null;
    }

    public UserResponseDto login(LoginRequestDto dto) {
        // 로그인 아이디로 회원 조회
        UserResponseDto user = loginMapper.findByLoginId(dto.getLoginId());
        // 아이디 없으면 에러
        if( user == null ) {
            log.debug("로그인 실패: 아이디를 찾을 수 없음 - {}", dto.getLoginId());
            return null;
        }
        // 비번 틀려도 에러 (평문 비교)
        String storedPassword = user.getPassword();
        boolean passwordMatches = dto.getPassword().equals(storedPassword);
        if( !passwordMatches ) {
            log.debug("로그인 실패: 비밀번호 불일치 - 사용자 ID: {}", user.getId());
            return null;
        }
        // 로그인 성공 시 마지막 로그인 시간 업데이트
        try {
            loginMapper.updateLastLoginAt(user.getId());
        } catch (Exception e) {
            log.warn("last_login_at 업데이트 실패 - 사용자 ID: {}", user.getId(), e);
        }
        log.debug("로그인 성공 - 사용자 ID: {}, 이름: {}", user.getId(), user.getName());
        return user;
    }

    public UserResponseDto findById(Integer id) {
        return loginMapper.findById(id);
    }

    public List<CategoryDto> getCategories() {
        return categoryMapper.findAllCategories();
    }

    public Boolean checkId(String loginId) {
        return loginMapper.countByLoginId(loginId) > 0;
    }

    public void sendEmailCode(String email) {
        // 컨트롤러로부터 지시받은 업무 처리(인증번호 발송을 위한 단계)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastIssuedAt = 
            (LocalDateTime) session.getAttribute("EMAIL_AUTH_ISSUED_AT");
        // 재발송 1분 제한
        if( lastIssuedAt != null) {
            long seconds =
                Duration.between(lastIssuedAt, now).getSeconds();
            if(seconds < 60) {
                throw new IllegalStateException(
                    "인증번호는 1분 후에 재발송할 수 있습니다."
                );
            }
        }
        // 인증번호 생성
        String authcode = createAuthCode();
        // 세션에 저장
        session.setAttribute("EMAIL_AUTH_CODE", authcode);
        session.setAttribute("EMAIL_AUTH_EMAIL", email);
        session.setAttribute("EMAIL_AUTH_ISSUED_AT", now);
    }

    private String createAuthCode() {
        return String.valueOf(
            (int)(Math.random() * 90000) + 100000
        );
    }

    // 인증번호 검증(컨트롤러로부터 받은 두번째 지시 처리 시작)
    public boolean checkEmailCode(String email, String inputCode) {
        String savedCode = (String) session.getAttribute("EMAIL_AUTH_CODE");
        String savedEmail = (String) session.getAttribute("EMAIL_AUTH_EMAIL");
        LocalDateTime issuedAt = (LocalDateTime) session.getAttribute("EMAIL_AUTH_ISSUED_AT");
        
        if(savedCode == null || savedEmail == null || issuedAt == null) {
            throw new IllegalStateException("인증번호를 다시 발급받아주세요.");
        } 
        if(!savedEmail.equals(email)) {
            return false;
        }
        
        // 1분 유효
        long seconds =
            Duration.between(issuedAt, LocalDateTime.now()).getSeconds();
        if(seconds > 60) {
            session.removeAttribute("EMAIL_AUTH_CODE");
            session.removeAttribute("EMAIL_AUTH_EMAIL");
            session.removeAttribute("EMAIL_AUTH_ISSUED_AT");

            throw new IllegalStateException("인증번호가 만료되었습니다. 재발급 해주세요.");
        }
        if( !savedEmail.equals(email)) return false;
        if( !savedCode.equals(inputCode)) return false;

        session.setAttribute("EMAIL_AUTH_SUCCESS",true);
        return true;
    }

    public Boolean checkEmailCode(EmailCodeDto dto) {
        // 기존 방식도 유지 (DB 기반)
        String savedCode = loginMapper.findAuthCodeByEmail(dto.getEmail());
        return savedCode != null && savedCode.equals(dto.getAuthCode());
    }

    public Integer insertUser(SignUpDto dto) {
        // 사용자 등록 (비밀번호 평문 저장)
        Integer result = loginMapper.insertUser(dto);
        
        // 주소 정보를 Address 테이블에 저장
        if (result > 0 && dto.getZipcode() != null && dto.getAddress() != null && dto.getAddressDetail() != null) {
            AddressDto addressDto = new AddressDto();
            addressDto.setUserId(dto.getUserId());
            addressDto.setRecipient(dto.getName()); // 수령인은 회원 이름
            addressDto.setPostcode(dto.getZipcode());
            addressDto.setAddress(dto.getAddress());
            addressDto.setDetailAddress(dto.getAddressDetail());
            addressDto.setRecipientPhone(dto.getPhone()); // 수령인 전화번호는 회원 전화번호
            addressDto.setIsDefault(true); // 회원가입 시 첫 주소이므로 기본 주소로 설정
            loginMapper.insertAddress(addressDto);
        }
        
        // 관심 카테고리 등록
        if (result > 0 && dto.getCategoryId() != null && !dto.getCategoryId().isEmpty()) {
            userSelectedCategoryMapper.insertUserCategories(dto.getUserId(), dto.getCategoryId());
        }
        
        return result;
    }

    // 아이디 찾기 로직
    public String findUserId(FindUserIdRequestDto dto) {
        UserResponseDto user = loginMapper.findByNameAndEmail(
            dto.getName(),
            dto.getEmail()
        );
        if( user == null) {
            throw new IllegalStateException("일치하는 회원이 없습니다.");
        }
        Boolean emailAuth = (Boolean) session.getAttribute("EMAIL_AUTH_SUCCESS");
        if(emailAuth == null || !emailAuth) {
            throw new IllegalStateException("이메일 인증이 필요합니다.");
        }
        // 아이디를 반환 (이메일로도 전송 가능)
        return user.getLoginId();
    }

    // 비밀번호 찾기 로직 (재설정을 위한 사용자 정보 반환)
    public UserResponseDto findPassword(FindPasswordRequestDto dto) {
        // 이메일 인증 확인
        Boolean emailAuth = (Boolean) session.getAttribute("EMAIL_AUTH_SUCCESS");
        if(emailAuth == null || !emailAuth) {
            throw new IllegalStateException("이메일 인증이 필요합니다.");
        }
        
        // 아이디와 이메일로 회원 조회
        UserResponseDto user = loginMapper.findByLoginIdAndEmail(
            dto.getLoginId(),
            dto.getEmail()
        );
        if(user == null) {
            throw new IllegalStateException("일치하는 회원이 없습니다.");
        }
        
        // 사용자 정보 반환 (비밀번호 재설정 페이지로 이동하기 위해)
        return user;
    }
}

