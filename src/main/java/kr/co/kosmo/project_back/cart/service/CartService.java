package kr.co.kosmo.project_back.cart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.cart.dto.CartDto;
import kr.co.kosmo.project_back.cart.mapper.CartMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartMapper cartMapper;

    // 장바구니 목록 조회
    public List<CartDto> getCartList(Integer userId) {
       return cartMapper.findCartListByUserId(userId);
    }
    // 장바구니 목록 추가
    public void addCartItem(CartDto dto) {
        cartMapper.insertOrUpdateCartItem(dto);
    }
}
