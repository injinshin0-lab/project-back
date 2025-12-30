package kr.co.kosmo.project_back.auth.dto.request;

import lombok.Getter;

@Getter
public class EmailSendRequestDto {
    private String email;
}

// 이메일 인증번호 발송 단계