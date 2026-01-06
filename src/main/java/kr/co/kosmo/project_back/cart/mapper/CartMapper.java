package kr.co.kosmo.project_back.cart.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.kosmo.project_back.cart.dto.CartDto;

@Mapper
public interface CartMapper {
    // 장바구니 항목 조회
    List<CartDto> findCartListByUserId(Integer userId);
    // 장바구니 추가
    void insertOrUpdateCartItem(CartDto dto);
}
