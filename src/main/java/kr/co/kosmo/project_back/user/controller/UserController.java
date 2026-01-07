package kr.co.kosmo.project_back.user.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.co.kosmo.project_back.auth.dto.response.RegisterResponseDto;
import kr.co.kosmo.project_back.user.dto.UserJoinDto;
import kr.co.kosmo.project_back.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

   // 회원가입 처리하라고 서비스에게 지시
   @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserJoinDto dto, HttpServletRequest request) {    // JSON -> DTO로 바꿔서 넣음(바인딩)
        try {
            Integer userId = userService.join(dto, request);

            return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponseDto(
                "회원가입 성공", userId));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new RegisterResponseDto(
                e.getMessage(),null));
        } 
    }

    // 아이디 중복체크
    @PostMapping("/check-username")
    public ResponseEntity<?> checkUsername(@Valid @RequestBody Map<String, String> request) {
        String loginId = request.get("userId");
        boolean isDuplicate = userService.isIdDuplicate(loginId);

        if(isDuplicate) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 아이디입니다.");
        }
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }
}





// 회원 되기 전 단계(회원가입 시작점)를 담당
// 회원가입과 같은 인증 진입 기능 담당