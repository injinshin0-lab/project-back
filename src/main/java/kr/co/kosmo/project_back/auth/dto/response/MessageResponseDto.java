package kr.co.kosmo.project_back.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageResponseDto {
    private String message;
}

// 공통으로 들어가는 메세지 단계