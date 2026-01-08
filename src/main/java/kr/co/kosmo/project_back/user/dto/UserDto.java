package kr.co.kosmo.project_back.user.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Integer userId;
    private String phone;
    private List<Integer> categoryId;
}









