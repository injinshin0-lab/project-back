package kr.co.kosmo.project_back.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.kosmo.project_back.auth.dto.request.EmailCodeDto;
import kr.co.kosmo.project_back.auth.dto.request.EmailSendRequestDto;
import kr.co.kosmo.project_back.auth.dto.request.ResetPasswordRequestDto;
import kr.co.kosmo.project_back.auth.dto.response.MessageResponseDto;
import kr.co.kosmo.project_back.auth.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserPasswordController {
    private final AuthService authService;
    // 인증번호 발송
    @PostMapping("/email-pw/send-code")
    public ResponseEntity<MessageResponseDto> sendEmailCode(
        @Valid @RequestBody EmailSendRequestDto dto) {
            authService.sendEmailCode(dto.getEmail());
            return ResponseEntity.ok(
                new MessageResponseDto("인증번호가 이메일로 발송되었습니다.")
            );
        }
    
        // 인증번호 검증 + Reset Token 발급
        @PostMapping("/email/verify-code")
        public ResponseEntity<Map<String, String>> verifyEmailCode(
            @Valid @RequestBody EmailCodeDto dto) {
            String resetToken = authService.verifyEmailCodeAndIssueResetToken(
                dto.getEmail(),
                dto.getAuthCode()
            );
            return ResponseEntity.ok(
                Map.of("resetToken", resetToken)        
                );
            }

        // 비번 재설정
        @PostMapping("/reset-password")
        public ResponseEntity<MessageResponseDto> resetPassword(
                @Valid @RequestBody ResetPasswordRequestDto dto) {
            authService.resetPasswordWithToken(
                dto.getToken(),
                dto.getNewPassword()
            );
            return ResponseEntity.ok(
                new MessageResponseDto("비밀번호가 변경되었습니다.")
                );
            }
        }


// 비밀번호 재설정 관련 요청 지시(service)