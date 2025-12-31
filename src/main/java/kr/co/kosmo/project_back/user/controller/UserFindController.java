package kr.co.kosmo.project_back.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.kosmo.project_back.auth.dto.request.FindUserIdRequestDto;
import kr.co.kosmo.project_back.auth.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserFindController {
    // 아이디 찾기
    private final AuthService authService;
    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@Valid @RequestBody FindUserIdRequestDto dto) {
        authService.findUserId(dto);
        return ResponseEntity.ok(
            Map.of("message", "아이디를 이메일로 전송했습니다.")
        );
    }
}







