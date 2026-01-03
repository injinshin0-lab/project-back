package kr.co.kosmo.project_back.review.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.kosmo.project_back.review.dto.response.ReviewResponseDto;
import kr.co.kosmo.project_back.review.service.ReviewService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    // 상품별 리뷰 목록 조회
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getReviews(
        @PathVariable Integer productId,
        @RequestParam(required = false) String sort 
    ) {
        return ResponseEntity.ok(
            reviewService.getReviewsByProduct(productId, sort)
        );
    }
}
