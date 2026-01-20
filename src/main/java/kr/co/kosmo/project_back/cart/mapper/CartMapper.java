package kr.co.kosmo.project_back.cart.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.kosmo.project_back.cart.dto.CartDto;

@Mapper
public interface CartMapper {
    // 장바구니 항목 조회
    List<CartDto> findCartListByUserId(Integer userId);
    // 장바구니 추가
    void insertCartItem(CartDto dto);
    // 수정
    void updateCartItemByCartItemId(Map<String, Object>params);
    // 장바구니 항목 삭제(단건)
    void deleteCartItem(@Param("userId") Integer userId, @Param("productId") Integer productId);
    // 장바구니 전체 삭제
    void deleteCart(Integer userId);
    // 회원 탈퇴
    void deleteByUserId(@Param("userId") Integer userId);
}
