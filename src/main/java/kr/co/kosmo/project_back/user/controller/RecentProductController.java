package kr.co.kosmo.project_back.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.user.dto.ProductDto;
import kr.co.kosmo.project_back.user.service.RecentProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recent-product")
@RequiredArgsConstructor
public class RecentProductController {
    private final RecentProductService recentProductService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getRecentProductList(@RequestParam Integer userId) {
        return ResponseEntity.ok(recentProductService.getRecentProductList(userId));
    }

    @DeleteMapping
    public ResponseEntity<Integer> deleteRecentProduct(
            @RequestParam Integer userId,
            @RequestParam Integer productId) {
        return ResponseEntity.ok(recentProductService.deleteRecentProduct(userId, productId));
    }
}

