package kr.co.kosmo.project_back.auth.dto.request;

import lombok.Getter;

@Getter
public class ResetPasswordRequestDto {
    private String token;
    private String newPassword;
}
// 비밀번호 재설정