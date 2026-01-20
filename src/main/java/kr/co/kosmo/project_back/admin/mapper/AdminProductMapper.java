package kr.co.kosmo.project_back.admin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import kr.co.kosmo.project_back.admin.dto.AdminCategoryDto;
import kr.co.kosmo.project_back.admin.dto.AdminProductRequestDto;
import kr.co.kosmo.project_back.admin.dto.AdminProductResponseDto;
import kr.co.kosmo.project_back.admin.dto.ProductSearchDto;

@Mapper
public interface AdminProductMapper {
    List<AdminCategoryDto> findAllCategories();
    Integer countProductList(ProductSearchDto searchDto);
    List<AdminProductResponseDto> findProductList(ProductSearchDto searchDto);
    AdminProductResponseDto findByProductId(Integer productId);
    Integer insertProduct(AdminProductRequestDto dto);
    Integer updateProduct(AdminProductRequestDto dto);
    Integer deleteProduct(Integer productId);
}


