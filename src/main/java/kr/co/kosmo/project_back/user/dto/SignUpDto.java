package kr.co.kosmo.project_back.user.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
    private Integer userId;
    
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,12}$", message = "아이디는 영문 소문자 및 숫자 4~12자여야 합니다.")
    private String loginId;
    
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$", 
             message = "비밀번호는 8~16자이며, 영문/숫자/특수문자를 모두 포함해야 합니다.")
    private String password;
    
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;
    
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", 
             message = "올바른 이메일 형식이 아닙니다. (예: user@example.com)")
    private String email;
    
    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    private String phone;
    
    @NotBlank(message = "우편번호는 필수 입력값입니다.")
    private String zipcode;
    
    @NotBlank(message = "기본주소는 필수 입력값입니다.")
    private String address;
    
    @NotBlank(message = "상세주소는 필수 입력값입니다.")
    private String addressDetail;
    
    @NotNull(message = "관심분야는 필수 선택항목입니다.")
    @Size(min = 1, message = "관심분야는 최소 1개 이상 선택해주세요.")
    @JsonProperty("categoryId")
    private List<Integer> categoryId;
}



