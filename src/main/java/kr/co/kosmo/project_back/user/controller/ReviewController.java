package kr.co.kosmo.project_back.user.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.user.dto.ReviewResponseDto;
import kr.co.kosmo.project_back.user.dto.ReviewRequestDto;
import kr.co.kosmo.project_back.user.service.ReviewService;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService userReviewService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(userReviewService.getReviewsByProduct(productId));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews() {
        return ResponseEntity.ok(userReviewService.getAllReviews());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(userReviewService.getReviewsByUserId(userId));
    }

    @GetMapping("/can-write/{userId}/{productId}")
    public ResponseEntity<Boolean> canWriteReview(
            @PathVariable Integer userId,
            @PathVariable Integer productId) {
        return ResponseEntity.ok(userReviewService.canWriteReview(userId, productId));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReview(@PathVariable Integer reviewId) {
        return ResponseEntity.ok(userReviewService.getReview(reviewId));
    }

    @PostMapping
    public ResponseEntity<Integer> insertReview(
            @RequestParam Integer userId,
            @RequestParam Integer productId,
            @RequestParam String content,
            @RequestParam Double rating,
            @RequestParam(required = false) List<MultipartFile> images) {
        ReviewRequestDto dto = new ReviewRequestDto();
        dto.setUserId(userId);
        dto.setProductId(productId);
        dto.setContent(content);
        dto.setRating(rating);
        dto.setImages(images);
        return ResponseEntity.ok(userReviewService.insertReview(dto));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Integer> updateReview(
            @PathVariable Integer reviewId,
            @RequestBody ReviewRequestDto dto) {
        dto.setReviewId(reviewId);
        // TODO: updateReview 메서드 구현 필요
        return ResponseEntity.ok(1);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Integer> deleteReview(@PathVariable Integer reviewId) {
        // TODO: deleteReview 메서드 구현 필요
        return ResponseEntity.ok(1);
    }
}


