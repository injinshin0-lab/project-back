package kr.co.kosmo.project_back.product.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Integer productId;
    private String productName;
    private String content;
    private Integer price;
    private String originName;
    private String imageUrl;
    private Boolean iswished;
    private Integer salesCount;
    private List<String> categoryNames;
}


// 결과용 DTO (검색 조건 X)