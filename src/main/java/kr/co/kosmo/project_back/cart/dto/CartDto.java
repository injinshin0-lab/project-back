package kr.co.kosmo.project_back.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDto {
    private Integer cartItemId;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private String productName;
    private Integer price;
    private String imageUrl;
}
