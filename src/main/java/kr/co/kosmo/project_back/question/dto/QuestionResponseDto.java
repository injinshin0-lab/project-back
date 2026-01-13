package kr.co.kosmo.project_back.question.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionResponseDto {
    private Integer questionId;
    private Integer userId;
    private Integer productId;
    private String title;
    private String content;
    private String questionStatus;
    private String type;
    private List<QuestionImageDto> questionImages;
    private LocalDateTime createdAt;
}
