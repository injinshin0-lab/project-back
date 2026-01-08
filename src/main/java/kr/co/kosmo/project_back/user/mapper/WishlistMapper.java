package kr.co.kosmo.project_back.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.kosmo.project_back.user.dto.ProductDto;

@Mapper
public interface WishlistMapper {
    List<ProductDto> findWishListByUserId(Integer userId);
    Integer insertWish(@Param("userId") Integer userId, @Param("productId") Integer productId);
    Integer deleteWish(@Param("userId") Integer userId, @Param("productId") Integer productId);
    Integer existsWish(@Param("userId") Integer userId, @Param("productId") Integer productId);
}

