package kr.co.kosmo.project_back.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private String productName;
    private Integer price;
    private String imageUrl;
}
