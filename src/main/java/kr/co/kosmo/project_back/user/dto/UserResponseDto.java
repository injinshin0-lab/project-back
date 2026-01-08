package kr.co.kosmo.project_back.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Integer id;
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String role;
}






