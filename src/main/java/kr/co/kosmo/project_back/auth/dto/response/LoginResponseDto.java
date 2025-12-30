package kr.co.kosmo.project_back.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String message;
    private Integer user_id;
    private String name;
}

// 로그인 응답 단계