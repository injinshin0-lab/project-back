package kr.co.kosmo.project_back.admin.dto;

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
    private Integer id;
    private Integer userId;
    private String userName;
    private Integer productId;
    private String title;
    private String content;
    private String questionStatus;
    private String type;

    private String productName;     // 상품명
    private String productImageUrl; // 상품 대표 이미지 경로


    private List<QuestionImageDto> questionImages;
    private QuestionAnswerDto answer; // 답변 정보
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
