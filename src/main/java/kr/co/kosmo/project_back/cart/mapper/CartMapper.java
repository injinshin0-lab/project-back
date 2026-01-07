package kr.co.kosmo.project_back.cart.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.kosmo.project_back.cart.dto.CartDto;

@Mapper
public interface CartMapper {
    // 장바구니 항목 조회
    List<Map<String, Object>> findCartListByUserId(Integer userId);
    // 장바구니 추가
    void insertCartItem(CartDto dto);
    // 수정
    void updateCartItemByCartItemId(Map<String, Object>params);
}
