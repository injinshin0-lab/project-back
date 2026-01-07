package kr.co.kosmo.project_back.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterResponseDto {
    private String message;
    private Integer user_id;
}
