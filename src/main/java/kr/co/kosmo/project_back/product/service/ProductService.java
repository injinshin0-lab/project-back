package kr.co.kosmo.project_back.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.product.dto.PageResponseDto;
import kr.co.kosmo.project_back.product.dto.ProductDto;
import kr.co.kosmo.project_back.product.dto.ProductSearchDto;
import kr.co.kosmo.project_back.product.mapper.ProductMapper;
import kr.co.kosmo.project_back.product.mapper.SearchMapper;
import kr.co.kosmo.project_back.recentproduct.mapper.RecentProductMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;
    private final SearchMapper searchMapper;
    private final RecentProductMapper recentProductMapper;

    // 상품상세 조회
    public ProductDto getProduct(Integer productId) {
        return productMapper.findByProductId(productId);
    }
    // 상품 검색 + 목록
    public PageResponseDto<ProductDto> searchProducts(ProductSearchDto searchDto) {
        // 전체개수
        int totalCount = productMapper.countProductList(searchDto);
        // 현재 페이지 목록
        List<ProductDto> list = productMapper.findProductList(searchDto);
        // 페이지 응답으로 묶음
        return PageResponseDto.of(
            list,
            totalCount,
            searchDto.getPage(),
            searchDto.getSize()
        );
    }
    // 인기 검색어 
    public List<String> getPopularKeywords() {
        return searchMapper.findPopularKeywords();
    }
}

    