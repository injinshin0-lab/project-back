package kr.co.kosmo.project_back.review.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDto {
    private Long reviewId;
    private Long userId;
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;
}
