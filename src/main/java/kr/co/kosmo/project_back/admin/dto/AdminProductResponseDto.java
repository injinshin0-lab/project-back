package kr.co.kosmo.project_back.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductResponseDto {
    private Integer productId;
    private String productName;
    private AdminCategoryDto category;
    private String originName;
    private String content;
    private Integer price;
    private String imageUrl;
}

