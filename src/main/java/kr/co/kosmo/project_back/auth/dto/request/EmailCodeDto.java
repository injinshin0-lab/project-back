package kr.co.kosmo.project_back.auth.dto.request;

import lombok.Getter;

@Getter
public class EmailCodeDto {
    private String email;
    private String authCode;
}

// 이메일 인증번호 확인 단계(회원가입, 아이디 찾기, 비밀번호 재설정)