package kr.co.kosmo.project_back.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.kosmo.project_back.user.dto.ProductDto;

@Mapper
public interface RecentProductMapper {
    Integer insertOrUpdateRecentProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);
    List<ProductDto> findRecentProductsByUserId(Integer userId);
    Integer deleteRecentProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);
}

