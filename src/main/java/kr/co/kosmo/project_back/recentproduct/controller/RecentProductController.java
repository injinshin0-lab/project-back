package kr.co.kosmo.project_back.recentproduct.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.kosmo.project_back.product.dto.ProductDto;
import kr.co.kosmo.project_back.recentproduct.service.RecentProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/recent-views")
@RequiredArgsConstructor
public class RecentProductController {
    private final RecentProductService recentProductService;

    // 최근 본 상품 조회
    @GetMapping("/{userId}")
    public List<ProductDto> getRecentProductList(
        @PathVariable Integer userId 
    ) {
        return recentProductService.getRecentProductList(userId);
    }
    // 최근 본 상품 추가
    @PostMapping("/{userId}/{productId}")
    public void addRecentProduct(
        @PathVariable Integer userId,
        @PathVariable Integer productId 
    ) {
        recentProductService.insertOrUpdateRecentProduct(userId, productId);
    }

}
