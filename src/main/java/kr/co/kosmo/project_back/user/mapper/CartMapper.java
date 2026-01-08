package kr.co.kosmo.project_back.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.kosmo.project_back.admin.dto.CartDto;

@Mapper
public interface CartMapper {
    List<CartDto> findCartListByUserId(Integer userId);
    Integer insertOrUpdateCartItem(CartDto dto);
    Integer deleteCartItem(@Param("userId") Integer userId, @Param("productId") Integer productId);
    Integer deleteCart(Integer userId);
}

