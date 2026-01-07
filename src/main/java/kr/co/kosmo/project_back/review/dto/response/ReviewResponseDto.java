package kr.co.kosmo.project_back.review.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDto {
    private Long reviewId;
    private Long userId;
    private String loginId;
    private String content;
    private Double rating;
    private List<ReviewImageDto> reviewImages;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
// 리뷰 조회 