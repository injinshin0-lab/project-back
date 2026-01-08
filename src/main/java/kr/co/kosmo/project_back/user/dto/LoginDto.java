package kr.co.kosmo.project_back.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String loginId;
    private String password;
    private Boolean saveId;
    private Boolean autoLogin;
}








