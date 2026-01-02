package kr.co.kosmo.project_back.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.kosmo.project_back.product.dto.PageResponseDto;
import kr.co.kosmo.project_back.product.dto.ProductDto;
import kr.co.kosmo.project_back.product.dto.ProductSearchDto;
import kr.co.kosmo.project_back.product.service.ProductService;
import kr.co.kosmo.project_back.product.vo.ProductVO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // 상품상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductVO> getProduct(
        @PathVariable Integer productId // URL 속 값을 꺼내 쓰기 위함
    ) {
        ProductVO product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    // 상품 목록 + 상품 검색 + 페이징
    @GetMapping
    public PageResponseDto<ProductDto> searchProducts(ProductSearchDto searchDto) {
        // 페이지 값이 없거나 0 이하로 들어오면 기본값으로 돌림
        if(searchDto.getPage() == null || searchDto.getPage() <= 0) {
            searchDto.setPage(1);
        }
        // size 값이 없거나 0 이하로 들어오면 기본값 10으로 돌림
        if(searchDto.getSize() == null || searchDto.getSize() <= 0) {
            searchDto.setSize(10);
        }
        // 검색조건을 서비스에 넘겨 로직 수행 후 결과 반환
        return productService.searchProducts(searchDto);
    }
}


// 검색결과 화면 처리