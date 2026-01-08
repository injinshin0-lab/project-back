package kr.co.kosmo.project_back.admin.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.AdminReviewResponseDto;
import kr.co.kosmo.project_back.admin.service.AdminReviewService;

@RestController
@RequestMapping("/api/v1/admin/review")
@RequiredArgsConstructor
public class AdminReviewController {
    private final AdminReviewService adminReviewService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<AdminReviewResponseDto>> getReviewsByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(adminReviewService.getReviewsByProduct(productId));
    }

    @GetMapping("/review/product/{reviewId}")
    public ResponseEntity<AdminReviewResponseDto> getReview(@PathVariable Integer reviewId) {
        return ResponseEntity.ok(adminReviewService.getReview(reviewId));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Integer> deleteReview(@PathVariable Integer reviewId) {
        return ResponseEntity.ok(adminReviewService.deleteReview(reviewId));
    }
}


