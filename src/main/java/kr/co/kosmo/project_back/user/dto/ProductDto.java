package kr.co.kosmo.project_back.user.dto;

import kr.co.kosmo.project_back.admin.dto.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Integer productId;
    private String productName;
    private CategoryDto category;
    private String originName;
    private String content;
    private Integer price;
    private String imageUrl;
    private Boolean isWished;
    private Double averageRating;
    private Integer reviewCount;
}

