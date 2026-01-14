package kr.co.kosmo.project_back.review.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyReviewResponseDto {
    private Integer reviewId;
    private String content;
    private Integer rating;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private Integer productId;
    private String productName;
    private String productImage;
    private List<ReviewImageDto> reviewImages;
}

// 내 리뷰 조회(마이페이지)