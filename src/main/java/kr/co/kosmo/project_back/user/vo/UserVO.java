package kr.co.kosmo.project_back.user.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVO {
    private Integer id;
    private String loginId;
    private String password;
    private String userName;
    private String email;
    private String phone;
    private String role;
    private String createdAt;
    private String updatedAt;
    private String lastLoginAt;

}
