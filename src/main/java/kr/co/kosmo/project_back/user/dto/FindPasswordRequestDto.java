package kr.co.kosmo.project_back.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPasswordRequestDto {
    @NotBlank(message = "아이디는 필수입력 항목입니다.")
    private String loginId;
    
    @Email
    @NotBlank(message = "이메일은 필수입력 항목입니다.")
    private String email;
}
// 비밀번호 찾기





