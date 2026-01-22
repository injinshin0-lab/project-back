package kr.co.kosmo.project_back.question.dto;

import java.time.LocalDateTime;
import java.util.List;

import kr.co.kosmo.project_back.admin.dto.QuestionAnswerDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionResponseDto {
    private Integer questionId;
    private Integer userId;
    private Integer productId;
    private String productName;
    private String title;
    private String content;
    private String questionStatus;
    private String type;
    private List<QuestionImageDto> questionImages;
    private LocalDateTime createdAt;

    private QuestionAnswerDto answer;
}
