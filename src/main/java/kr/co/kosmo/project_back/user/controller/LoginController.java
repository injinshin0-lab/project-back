package kr.co.kosmo.project_back.user.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.user.dto.SignUpDto;
import kr.co.kosmo.project_back.user.dto.UserResponseDto;
import kr.co.kosmo.project_back.user.service.LoginService;
import kr.co.kosmo.project_back.user.dto.EmailCodeDto;
import kr.co.kosmo.project_back.user.dto.EmailSendRequestDto;
import kr.co.kosmo.project_back.user.dto.FindUserIdRequestDto;
import kr.co.kosmo.project_back.user.dto.FindPasswordRequestDto;
import kr.co.kosmo.project_back.user.dto.LoginRequestDto;
import kr.co.kosmo.project_back.user.dto.AutoLoginResponseDto;
import kr.co.kosmo.project_back.user.dto.LoginResponseDto;
import kr.co.kosmo.project_back.user.dto.MessageResponseDto;
import kr.co.kosmo.project_back.admin.dto.CategoryDto;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto dto,
        HttpServletRequest request) {
        UserResponseDto user = loginService.login(dto);
        if(user == null) {
            return ResponseEntity.status(401)
            .body(new MessageResponseDto("아이디 또는 비밀번호가 일치하지 않습니다."));
        }

        // 로그인 시 세션 생성 (id, role, name 저장)
        HttpSession session = request.getSession();
        session.setAttribute("LOGIN_USER", user.getId());
        session.setAttribute("LOGIN_USER_ROLE", user.getRole());
        session.setAttribute("LOGIN_USER_NAME", user.getName());
        return ResponseEntity.ok(
            new LoginResponseDto(
                "로그인 성공",
                user.getId(),
                user.getName()
            )
        );
    }

    @PostMapping("/auto-login")
    public ResponseEntity<?> autologin(HttpServletRequest request) {
        // 기존 세션 조회 (만약 없다면 null 반환)
        HttpSession session = request.getSession(false);
        // 세션이 없으면 로그인 불가
        if(session == null) {
            return ResponseEntity.status(401).body(new MessageResponseDto("로그인 상태가 아닙니다."));
        }

        // 세션에서 id, role, name 가져오기
        Integer userId = (Integer) session.getAttribute("LOGIN_USER");
        String userRole = (String) session.getAttribute("LOGIN_USER_ROLE");
        String userName = (String) session.getAttribute("LOGIN_USER_NAME");
        
        if(userId == null || userRole == null || userName == null) {    // 로그인 안 한 상태
            return ResponseEntity.status(401).body(new MessageResponseDto("로그인 상태가 아닙니다."));
        }
        
        // 세션 정보로 응답 (DB 조회 없이)
        return ResponseEntity.ok(
            new AutoLoginResponseDto(
                true,
                userId,
                userName,
                userRole
            )
        );
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(loginService.getCategories());
    }

    @PostMapping("/check-id")
    public ResponseEntity<Boolean> checkId(@RequestParam String loginId) {
        return ResponseEntity.ok(loginService.checkId(loginId));
    }

    @PostMapping("/email/send-code")
    public ResponseEntity<MessageResponseDto> sendEmailCode(
            @RequestBody EmailSendRequestDto dto) {
        loginService.sendEmailCode(dto.getEmail());
        return ResponseEntity.ok(
            new MessageResponseDto("인증번호가 이메일로 발송되었습니다.")
        );
    }

    @PostMapping("/email/verify-code")
    public ResponseEntity<MessageResponseDto> verifyEmailCode(
        @RequestBody EmailCodeDto dto,
        HttpServletRequest request) {
            boolean result = loginService.checkEmailCode(
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

    @PostMapping("/signup")
    public ResponseEntity<Integer> insertUser(@Valid @RequestBody SignUpDto dto) {
        return ResponseEntity.ok(loginService.insertUser(dto));
    }

    // 아이디 찾기 (이메일 인증 후 아이디 반환)
    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@Valid @RequestBody FindUserIdRequestDto dto) {
        String loginId = loginService.findUserId(dto);
        return ResponseEntity.ok(
            Map.of("loginId", loginId, "message", "아이디를 찾았습니다.")
        );
    }

    // 비밀번호 찾기 (이메일 인증 후 사용자 정보 반환 - 재설정 페이지로 이동)
    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@Valid @RequestBody FindPasswordRequestDto dto) {
        UserResponseDto user = loginService.findPassword(dto);
        // 사용자 ID만 반환 (비밀번호 재설정 페이지에서 사용)
        return ResponseEntity.ok(
            Map.of("userId", user.getId(), "loginId", user.getLoginId(), "message", "비밀번호 재설정 페이지로 이동하세요.")
        );
    }
}