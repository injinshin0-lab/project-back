package kr.co.kosmo.project_back.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonPropertyOrder({ "isAuthenticated", "user_id", "name" })
@Getter
@AllArgsConstructor
public class AutoLoginResponseDto {
    @JsonProperty("isAuthenticated")
    private boolean authenticated;
    private Integer user_id;
    private String name;
}
