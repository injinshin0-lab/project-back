package kr.co.kosmo.project_back.product.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import kr.co.kosmo.project_back.product.dto.ProductDto;
import kr.co.kosmo.project_back.product.dto.ProductSearchDto;

@Mapper
public interface ProductMapper {
    // 상품목록 조회
    List<ProductDto> findAllProducts();
    // 상품상세 조회
    ProductDto findByProductId(Integer productId);  
    // 상품검색 기능
    int countProductList(ProductSearchDto searchDto);
    List<ProductDto> findProductList(ProductSearchDto searchDto);
}
