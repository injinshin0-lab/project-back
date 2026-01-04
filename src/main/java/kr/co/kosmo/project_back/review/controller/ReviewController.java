package kr.co.kosmo.project_back.review.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.kosmo.project_back.review.dto.request.ReviewRequestDto;
import kr.co.kosmo.project_back.review.dto.response.ReviewCreateResponseDto;
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

    // 리뷰 작성
    @PostMapping("/{productId}/reviews")
    public ResponseEntity<ReviewCreateResponseDto> insertReview(
        @PathVariable Integer productId,
        @RequestBody ReviewRequestDto dto
    ) {
        Long reviewId = reviewService.insertReview(productId, dto);
        return ResponseEntity
            .status(201)
            .body(new ReviewCreateResponseDto(reviewId, "후기 등록 완료"));
    }

    // 리뷰 수정
    @PutMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<Void> updateReview(
        @PathVariable Integer productId,
        @PathVariable Long reviewId,
        @RequestBody ReviewRequestDto dto
    ) {
        reviewService.updateReview(reviewId, dto);
        return ResponseEntity.noContent().build();
    }

    // 리뷰 삭제
    @DeleteMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
        @PathVariable Integer productId,
        @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
