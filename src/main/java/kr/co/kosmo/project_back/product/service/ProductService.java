package kr.co.kosmo.project_back.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.product.dto.PageResponseDto;
import kr.co.kosmo.project_back.product.dto.ProductDto;
import kr.co.kosmo.project_back.product.dto.ProductSearchDto;
import kr.co.kosmo.project_back.product.mapper.ProductMapper;
import kr.co.kosmo.project_back.product.vo.ProductVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;

    // 상품상세 조회
    public ProductVO getProduct(Integer productId) {
        return productMapper.findByProductId(productId);
    }
    // 상픔 검색 
    public PageResponseDto<ProductDto> searchProducts
        (ProductSearchDto searchDto) {
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
}

    

// 검색결과 가공 