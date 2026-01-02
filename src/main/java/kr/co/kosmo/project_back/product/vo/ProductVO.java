package kr.co.kosmo.project_back.product.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVO {
    private Integer id;
    private String productName;
    private String content;
    private Integer price;
    private String productStatus;
    private String originName;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
