package kr.co.kosmo.project_back.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.kosmo.project_back.admin.dto.CategoryDto;
import kr.co.kosmo.project_back.admin.dto.ProductSearchDto;
import kr.co.kosmo.project_back.user.dto.ProductDto;

@Mapper
public interface ProductMapper {
    List<CategoryDto> findAllCategories();
    Integer countProductList(ProductSearchDto searchDto);
    List<ProductDto> findProductList(ProductSearchDto searchDto);
    ProductDto findByProductId(@Param("productId") Integer productId, @Param("userId") Integer userId);
}
