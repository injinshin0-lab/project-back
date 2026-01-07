package kr.co.kosmo.project_back.cart.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.cart.dto.CartDto;
import kr.co.kosmo.project_back.cart.mapper.CartMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartMapper cartMapper;

    // 장바구니 목록 조회
    public List<Map<String, Object>> getCartList(Integer userId) {
       return cartMapper.findCartListByUserId(userId);
    }
    // 장바구니 추가
    public void addCartItem(CartDto dto) {
         cartMapper.insertCartItem(dto);
        }    
    // 수정
    public void updateCartItem(
            Integer userId,
            Integer cartItemId,
            Integer quantity
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("cartItemId", cartItemId);
        params.put("quantity", quantity);
        cartMapper.updateCartItemByCartItemId(params);
    }
    // 장바구니 항목 삭제(단건)
    public void removeCartItem(Integer userId, Integer productId) {
        cartMapper.deleteCartItem(userId, productId);
    }
    // 장바구니 전체 삭제
    public void removeCart(Integer userId) {
        cartMapper.deleteCart(userId);
    }

}

