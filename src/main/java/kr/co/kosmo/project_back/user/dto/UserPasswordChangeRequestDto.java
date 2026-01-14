package kr.co.kosmo.project_back.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserPasswordChangeRequestDto {
    @NotBlank
    private String currentPassword;
    @NotBlank
    private String newPassword;
}
