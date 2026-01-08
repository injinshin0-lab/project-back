package kr.co.kosmo.project_back.product.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.product.dto.PageResponseDto;
import kr.co.kosmo.project_back.product.dto.ProductDto;
import kr.co.kosmo.project_back.product.dto.ProductSearchDto;
import kr.co.kosmo.project_back.product.service.ProductService;
import kr.co.kosmo.project_back.recentproduct.service.RecentProductService;
import kr.co.kosmo.project_back.review.service.ReviewService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final RecentProductService recentProductService;
    private final ProductService productService;
    private final ReviewService reviewService;

    // 상품상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(
        @PathVariable Integer productId, // URL 속 값을 꺼내 쓰기 위함
        HttpSession session
    ) {
        ProductDto product = productService.getProduct(productId);
        // 로그인 된 사용자만 최근 본 상품 저장
        Integer userId = (Integer) session.getAttribute("LOGIN_USER");
        if(userId != null) {
            recentProductService.insertOrUpdateRecentProduct(userId, productId);
        }
        return ResponseEntity.ok(product);
    }

    // 상품 목록 + 상품 검색 + 페이징
    @GetMapping
    public PageResponseDto<ProductDto> searchProducts(
        @ModelAttribute ProductSearchDto searchDto, HttpSession session) {
        // 페이지 값이 없거나 0 이하로 들어오면 기본값으로 돌림
        if(searchDto.getPage() == null || searchDto.getPage() <= 0) {
            searchDto.setPage(1);
        }
        // size 값이 없거나 0 이하로 들어오면 기본값 10으로 돌림
        if(searchDto.getSize() == null || searchDto.getSize() <= 0) {
            searchDto.setSize(10);
        }
        // 최근검색어 화면에 응답
        List<String> recentKeywords =
            (List<String>) session.getAttribute("recentKeywords");
        if(recentKeywords == null) {
            recentKeywords = new ArrayList<>();
        }
        // 검색 기록 세션 저장
        if(searchDto.getKeyword() != null && !searchDto.getKeyword().isBlank()) {
            recentKeywords.add(searchDto.getKeyword());
        if(recentKeywords.size() > 10) {
            recentKeywords.remove(0);
            }
            session.setAttribute("recentKeywords", recentKeywords);
            }
        // 상품검색
        PageResponseDto<ProductDto> response = 
            productService.searchProducts(searchDto);
        // 최근검색어 응답
        response.setRecentKeywords(recentKeywords);
        return response;
    }
    // 인기 검색어
    @GetMapping("/popular-searches")
    public Map<String, List<String>> getPopularSearches() {
        return Map.of(
            "popularKeywords",
            productService.getPopularKeywords()
        );
    }
}
