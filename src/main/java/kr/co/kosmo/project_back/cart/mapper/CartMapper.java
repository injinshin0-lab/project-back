package kr.co.kosmo.project_back.cart.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.kosmo.project_back.cart.dto.CartDto;

@Mapper
public interface CartMapper {
    // 장바구니 항목 조회
    List<CartDto> findCartListByUserId(Integer userId);
    // 장바구니 추가, 수정
    void insertOrUpdateCartItem(CartDto dto);
    // 장바구니 항목 삭제
    void deleteCartItem(@Param("userId")Integer userId, @Param("productId")Integer productId);
    // 장바구니 전체 삭제
    void deleteCart(@Param("userId")Integer userId);
}
