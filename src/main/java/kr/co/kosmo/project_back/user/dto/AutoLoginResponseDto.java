package kr.co.kosmo.project_back.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonPropertyOrder({ "isAuthenticated", "user_id", "name", "role" })
@Getter
@AllArgsConstructor
public class AutoLoginResponseDto {
    @JsonProperty("isAuthenticated")
    private boolean authenticated;
    private Integer user_id;
    private String name;
    private String role;
}

// 자동로그인 응답 단계





