package kr.co.kosmo.project_back.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import kr.co.kosmo.project_back.cart.dto.CartDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDto {
    private Integer orderId;
    private Integer userId;
    private String payType;
    private Integer addressId;
    private Integer totalPrice;
    private LocalDateTime createdAt;
    private List<CartDto> orderItems;
}
