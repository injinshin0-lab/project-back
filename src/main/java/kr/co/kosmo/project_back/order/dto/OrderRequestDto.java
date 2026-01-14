package kr.co.kosmo.project_back.order.dto;

import java.util.List;

import kr.co.kosmo.project_back.cart.dto.CartDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {
    private Integer orderId;
    private Integer userId;
    private Integer addressId;
    private Integer shippingAddressId;

    private String postcode;
    private String address1;
    private String address2;
    
    private String paymentMethod;
    private Boolean useCartItems;
    // 바로주문
    private List<CartDto> items;
    private Integer paymentPrice;
}
