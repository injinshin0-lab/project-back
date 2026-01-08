package kr.co.kosmo.project_back.user.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto {
    private Integer questionId;
    private Integer userId;
    private Integer productId;
    private String title;
    private String content;
    private String questionStatus;
    private String type;
    private List<QuestionImageDto> questionImages;
    private QuestionAnswerDto answer; // 답변 정보
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}

