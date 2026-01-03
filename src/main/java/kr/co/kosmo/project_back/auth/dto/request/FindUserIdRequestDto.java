package kr.co.kosmo.project_back.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FindUserIdRequestDto {
    @NotBlank
    private String name;
    
    @Email
    @NotBlank
    private String email;
}
// 회원 아이디 찾기