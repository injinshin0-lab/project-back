package kr.co.kosmo.project_back.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.auth.dto.request.FindUserIdRequestDto;
import kr.co.kosmo.project_back.auth.dto.request.LoginRequestDto;
import kr.co.kosmo.project_back.mail.service.MailService;
import kr.co.kosmo.project_back.user.mapper.UserMapper;
import kr.co.kosmo.project_back.user.vo.UserVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;
    private final MailService mailService;
    

    public UserVO login(LoginRequestDto dto) {
        // 로그인 아이디로 회원 조회
        UserVO user = userMapper.selectByLoginId(dto.getLoginId());
        // 아이디 없으면 에러
        if( user == null ) return null;
        // 비번 틀려도 에러
        if( !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return null;
        }
        return user;
    }
    public UserVO findById(Integer id) {
        return userMapper.selectById(id);
    }
    public void sendEmailCode(String email) {
        // 컨트롤러로부터 지시받은 업무 처리하는 단계 //
        // 인증번호 생성
        String authcode = createAuthCode();
        // 세션에 저장
        session.setAttribute("EMAIL_AUTH_CODE", authcode);
        session.setAttribute("EMAIL_AUTH_EMAIL", email);
        // 유효시간 1분
        session.setMaxInactiveInterval(60);
        // 이메일 발송
        mailService.sendAuthCode(email, authcode);
    }
        private String createAuthCode() {
            return String.valueOf(
                (int)(Math.random() * 90000) + 100000
            );
        }   // 인증번호를 어떻게 만들었는지 구현 설명

    // 인증번호 검증(컨트롤러로부터 받은 두번째 지시 처리 시작)
    public boolean checkEmailCode(String email, String inputCode) {
        String savedCode = (String) session.getAttribute("EMAIL_AUTH_CODE");
        String savedEmail = (String) session.getAttribute("EMAIL_AUTH_EMAIL");
        
        if(savedCode == null || savedEmail == null) {
            return false;
        } 
        return savedEmail.equals(email) && savedCode.equals(inputCode);
    }

    // 아이디 찾기 로직
    public void findUserId(FindUserIdRequestDto dto) {
        UserVO user = userMapper.findByNameAndEmail(
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
        mailService.sendUserIdMail(
            dto.getEmail(),
            user.getLoginId()
        );
    }
}






// MailService를 호출. 인증번호 생성
// AuthController -> AuthService -> MailService 순서