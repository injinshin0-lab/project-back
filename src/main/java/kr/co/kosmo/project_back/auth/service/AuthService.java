package kr.co.kosmo.project_back.auth.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.auth.dto.request.FindUserIdRequestDto;
import kr.co.kosmo.project_back.auth.dto.request.LoginRequestDto;
import kr.co.kosmo.project_back.mail.service.MailService;
import kr.co.kosmo.project_back.user.dto.UserDto;
import kr.co.kosmo.project_back.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;
    private final MailService mailService;
    

    public UserDto login(LoginRequestDto dto) {
        // 로그인 아이디로 회원 조회
        UserDto user = userMapper.selectByLoginId(dto.getLoginId());
        // 아이디 없으면 에러
        if( user == null ) return null;
        // 비번 틀려도 에러
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return null;
        }
        return user;
    }
    public UserDto findById(Integer id) {
        return userMapper.selectById(id);
    }
    public void sendEmailCode(String email) {
        // 컨트롤러로부터 지시받은 업무 처리(인증번호 발송을 위한 단계) //
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastIssuedAt = 
            (LocalDateTime) session.getAttribute("EMAIL_AUTH_ISSUED_AT");
        // 재발송 1분 제한
        if(lastIssuedAt != null) {
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
        if(!savedEmail.equals(email)) return false;
        if(!savedCode.equals(inputCode)) return false;

        session.setAttribute("EMAIL_AUTH_SUCCESS",true);
        return true;
    }

    // 아이디 찾기 로직
    public void findUserId(FindUserIdRequestDto dto) {
        UserDto user = userMapper.findByNameAndEmail(
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

    // 인증번호 검증 + Reset Token 발급
    public String verifyEmailCodeAndIssueResetToken(String email, String authCode) {
        boolean success = checkEmailCode(email, authCode);
        if(!success) {
            throw new IllegalStateException("인증번호가 올바르지 않습니다.");
        }
        // Reset Token
        String resetToken = UUID.randomUUID().toString();
        // session 저장
        session.setAttribute("RESET_PW_TOKEN", resetToken);
        session.setAttribute("RESET_PW_EMAIL", email);
        session.setAttribute("RESET_PW_ISSUED_AT", LocalDateTime.now());

        return resetToken;
    }

     // Reset Token으로 비밀번호 재설정
     public void resetPasswordWithToken(String token, String newPassword) {

        String savedToken = (String) session.getAttribute("RESET_PW_TOKEN");
        String email = (String) session.getAttribute("RESET_PW_EMAIL");
        LocalDateTime issuedAt = 
            (LocalDateTime) session.getAttribute("RESET_PW_ISSUED_AT");
        if(savedToken == null || email == null || issuedAt == null) {
            throw new IllegalStateException("비밀번호 재설정 인증이 필요합니다.");
        }

        // 토큰 일치 확인
        if(!savedToken.equals(token)) {
            throw new IllegalStateException("유효하지 않은 토큰입니다.");
        }

        // 토큰 만료 (5분)
        long seconds = 
            Duration.between(issuedAt, LocalDateTime.now()).getSeconds();
        if (seconds > 300) {
            clearResetSession();
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }

        // 비밀번호 암호화
        String encodedPw = passwordEncoder.encode(newPassword);

        // DB 업뎃
        userMapper.updatePasswordByEmail(email, encodedPw);

        // 사용 끝난 인증 정보 제거
        clearResetSession();
     }

     private void clearResetSession() {
        session.removeAttribute("RESET_PW_TOKEN");
        session.removeAttribute("RESET_PW_EMAIL");
        session.removeAttribute("RESET_PW_ISSUED_AT");

        session.removeAttribute("EMAIL_AUTH_CODE");
        session.removeAttribute("EMAIL_AUTH_EMAIL");
        session.removeAttribute("EMAIL_AUTH_ISSUED_AT");
        session.removeAttribute("EMAIL_AUTH_SUCCESS");
     }
}



// MailService를 호출. 인증번호 생성
// AuthController -> AuthService -> MailService 순서