package kr.co.kosmo.project_back.auth.dto.request;

import lombok.Getter;

@Getter
public class EmailVerifyRequestDto {
    private String email;
    private String code;
}

// 이메일 검증용 