package kr.co.kosmo.project_back.cart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDto {
    @JsonIgnore
    private Integer userId;
    private Integer cartItemId;
    private Integer productId;
    private String productName;
    private Integer quantity;
    private Integer price;
}
