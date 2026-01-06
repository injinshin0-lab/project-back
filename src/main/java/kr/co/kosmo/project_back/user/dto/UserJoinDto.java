// 회원가입 시 사용자로부터 받는 값 저장소
package kr.co.kosmo.project_back.user.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserJoinDto {
    private Integer id;
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    // 규칙: 제한없음(한글ㅇ)
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    // 규칙: 4글자 이상(한글X)
    @Pattern(regexp = "^[^가-힣]{4,}$", 
             message = "비밀번호는 4자 이상이며 한글은 사용할 수 없습니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    // 규칙: 실제 이메일 형식 (도메인 .com, .net 등 포함 여부 체크)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", 
             message = "올바른 이메일 형식이 아닙니다. (예: user@example.com)")
    private String email;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    private String phone;

    @NotBlank(message = "우편번호는 필수 입력값입니다.")
    private String postcode;

    @NotBlank(message = "기본주소는 필수 입력값입니다.")
    private String address;

    @NotBlank(message = "상세주소는 필수 입력값입니다.")
    private String addressDetail;

    @NotNull(message = "관심분야는 필수 선택항목입니다.")
    @Size(min = 1, message = "관심분야는 최소 1개 이상 선택해주세요.")
    @JsonProperty("categories")
    private List<Integer> categories;
}
