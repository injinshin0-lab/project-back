package kr.co.kosmo.project_back.user.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import kr.co.kosmo.project_back.admin.dto.CartDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private Integer orderId;
    private Integer userId;
    private String payType;
    private Integer addressId;
    private Integer totalPrice;
    private List<CartDto> cartItems;
}

