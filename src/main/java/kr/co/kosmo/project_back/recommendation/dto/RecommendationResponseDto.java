package kr.co.kosmo.project_back.recommendation.dto;

import kr.co.kosmo.project_back.user.dto.CategoryDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendationResponseDto {
    private Integer productId;
    private String productName;
    private CategoryDto category;
    private String originName;
    private String content;
    private Integer price;
    private String imageUrl;
    private Integer rank;
}
