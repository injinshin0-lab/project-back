package kr.co.kosmo.project_back.product.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import kr.co.kosmo.project_back.product.dto.ProductDto;
import kr.co.kosmo.project_back.product.dto.ProductSearchDto;

@Mapper
public interface ProductMapper {
    // 상품상세 조회
    ProductDto findByProductId(Integer productId); 

    // 상품 가격만 조회
    Integer findPriceByProductId(Integer productId);
    // 상품검색 + 목록
    int countProductList(ProductSearchDto searchDto);
    List<ProductDto> findProductList(ProductSearchDto searchDto);

    // 인기 상품 전용 메서드 추가
    List<ProductDto> findTopSalesProducts();

    Integer updateViewCount(Integer productId);

    List<ProductDto> findTopViewProducts();
}
