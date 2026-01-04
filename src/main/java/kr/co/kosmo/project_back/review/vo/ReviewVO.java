package kr.co.kosmo.project_back.review.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewVO {
    private Long reviewId;
    private Integer productId;
    private Long userId;
    private String content;
    private Integer rating;
}
