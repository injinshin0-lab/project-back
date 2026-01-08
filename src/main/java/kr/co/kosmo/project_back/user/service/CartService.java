package kr.co.kosmo.project_back.user.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.CartDto;
import kr.co.kosmo.project_back.user.mapper.CartMapper;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartMapper cartMapper;

    public List<CartDto> getCartList(Integer userId) {
        List<CartDto> cartList = cartMapper.findCartListByUserId(userId);
        // 이미지 URL에 uploads/ 접두사 추가
        if (cartList != null) {
            for (CartDto cart : cartList) {
                try {
                    if (cart != null && cart.getImageUrl() != null && !cart.getImageUrl().isEmpty()) {
                        String imageUrl = cart.getImageUrl().trim();
                        if (!imageUrl.startsWith("/uploads/") && !imageUrl.startsWith("uploads/")) {
                            cart.setImageUrl("/uploads/" + imageUrl);
                        }
                    }
                } catch (Exception e) {
                    // 이미지 URL 처리 실패 시 무시하고 계속 진행
                }
            }
        }
        return cartList;
    }

    public Integer addCartItem(CartDto dto) {
        // 기존에 있으면 수량 증가, 없으면 새로 추가 (간단하게 INSERT만 사용)
        return cartMapper.insertOrUpdateCartItem(dto);
    }

    public Integer removeCartItem(CartDto dto) {
        return cartMapper.deleteCartItem(dto.getUserId(), dto.getProductId());
    }

    public Integer removeCart(CartDto dto) {
        return cartMapper.deleteCart(dto.getUserId());
    }
}

