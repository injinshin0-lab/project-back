package kr.co.kosmo.project_back.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewCreateResponseDto {
    private Long reviewId; 
    private String message;
}
// 리뷰 작성 응답