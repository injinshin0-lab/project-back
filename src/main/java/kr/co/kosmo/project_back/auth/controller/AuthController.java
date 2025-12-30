package kr.co.kosmo.project_back.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.co.kosmo.project_back.auth.dto.request.EmailCodeDto;
import kr.co.kosmo.project_back.auth.dto.request.EmailSendRequestDto;
import kr.co.kosmo.project_back.auth.dto.request.LoginRequestDto;
import kr.co.kosmo.project_back.auth.dto.response.AutoLoginResponseDto;
import kr.co.kosmo.project_back.auth.dto.response.LoginResponseDto;
import kr.co.kosmo.project_back.auth.dto.response.MessageResponseDto;
import kr.co.kosmo.project_back.auth.service.AuthService;
import kr.co.kosmo.project_back.user.vo.UserVO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // 사용자로부터 인증번호 달라는 요청을 받고 바로 시작되는 단계
    // 요청을 받고, 요청에 담긴 값을 꺼내 서비스를 호출하여 전달
    @PostMapping("/email/send-code")
    public ResponseEntity<MessageResponseDto> sendEmailCode(
            @RequestBody EmailSendRequestDto dto) {
        authService.sendEmailCode(dto.getEmail());  // 서비스에게 일 시킨다
        return ResponseEntity.ok(
            new MessageResponseDto("인증번호가 이메일로 발송되었습니다.")
        );
    }
    // 인증코드 확인절차(사용자로부터 두번째 요청 받음)
    @PostMapping("/email/verify-code")
    public ResponseEntity<MessageResponseDto> verifyEmailCode(
        @RequestBody EmailCodeDto dto,
        HttpServletRequest request) {
            boolean result = authService.checkEmailCode(   // 서비스에게 이거 맞아? 라고 묻는 지점
                dto.getEmail(),
                dto.getAuthCode()
            );
            if (!result) {
                return ResponseEntity.status(401)
                .body(new MessageResponseDto("인증번호가 올바르지 않거나 만료되었습니다."));
            }
            HttpSession session = request.getSession();
            session.setAttribute("EMAIL_AUTH_SUCCESS", true);
            return ResponseEntity.ok(
                new MessageResponseDto("이메일 인증 성공")
            );
        }
    // 로그인    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto dto,
        HttpServletRequest request) {
        UserVO user = authService.login(dto);
        if(user == null) {
            return ResponseEntity.status(401)
            .body(new MessageResponseDto("아이디 또는 비밀번호가 일치하지 않습니다."));
        }
        // 로그인 시 세션 생성
        HttpSession session = request.getSession();
        session.setAttribute("LOGIN_USER", user.getId());
        return ResponseEntity.ok(
            new LoginResponseDto(
                "로그인 성공",
                user.getId(),
                user.getName()
            )
        );
    }
    // 자동 로그인
    @PostMapping("/auto-login")
    public ResponseEntity<?> autologin(HttpServletRequest request) {
        // 기존 세션 조회 (만약 없다면 null 반환)
        HttpSession session = request.getSession(false);
        // 세션이 없으면 로그인 불가
        if(session == null) {
            return ResponseEntity.status(401).body(new MessageResponseDto("로그인 상태가 아닙니다."));
        }
        // 세션은 있지만 로그인은 안했다면
        Integer userId = (Integer) session.getAttribute("LOGIN_USER");
        if(userId == null) {    // 로그인 안 한 상태
            return ResponseEntity.status(401).body(new MessageResponseDto("로그인 상태가 아닙니다."));
        }
        UserVO user = authService.findById(userId);
        return ResponseEntity.ok(
            new AutoLoginResponseDto(
                true,
                userId,
                user.getName()
            )
        );
    }
    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<MessageResponseDto> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();   // 세션 제거
        }
        return ResponseEntity.ok(
            new MessageResponseDto("로그아웃 및 세션 무효화 성공")
        );
    }
}
