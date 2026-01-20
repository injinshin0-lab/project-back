package kr.co.kosmo.project_back.admin.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDto {
    private Integer id;
    private Integer userId;
    private String userLoginId; // 프론트에서 보내는 유저 아이디
    private String type;
    private String content;

    private int userCount;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
