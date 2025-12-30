package kr.co.kosmo.project_back.auth.dto.request;

import lombok.Getter;

@Getter
public class EmailCodeDto {
    private String email;
    private String authCode;
}

// 이메일 검증용 