package kr.co.kosmo.project_back.user.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {
    private String userName;
    private String phone;
    private List<Integer> category;
}


// 회원정보 수정용