package kr.co.kosmo.project_back.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.product.dto.PageResponseDto;
import kr.co.kosmo.project_back.product.dto.ProductDto;
import kr.co.kosmo.project_back.product.dto.ProductSearchDto;
import kr.co.kosmo.project_back.product.mapper.ProductMapper;
import kr.co.kosmo.project_back.product.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;
    private final SearchMapper searchMapper;

    // 상품상세 조회
    public ProductDto getProduct(Integer productId) {
        productMapper.updateViewCount(productId);
        return productMapper.findByProductId(productId);
    }

    // 상품 검색 + 목록
    public PageResponseDto<ProductDto> searchProducts(ProductSearchDto searchDto) {
        int totalCount = productMapper.countProductList(searchDto);
        List<ProductDto> list = productMapper.findProductList(searchDto);
        return PageResponseDto.of(list, totalCount, searchDto.getPage(), searchDto.getSize());
    }

    // ✅ 컴파일 에러 해결을 위해 이 메서드를 다시 추가하세요!
    public List<String> getPopularKeywords() {
        return searchMapper.findPopularKeywords();
    }

    // 인기 상품
    public List<ProductDto> getPopularProducts() {
        return productMapper.findTopSalesProducts();
    }

    // 조회순 상품 가져오기
    public List<ProductDto> getTopViewProducts() {
        return productMapper.findTopViewProducts();
    }
    
    // ❌ formatProductImageUrl 함수는 절대로 다시 만들지 마세요! (중복의 원인)
}