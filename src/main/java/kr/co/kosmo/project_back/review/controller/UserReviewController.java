package kr.co.kosmo.project_back.review.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.review.dto.response.MyReviewResponseDto;
import kr.co.kosmo.project_back.review.service.ReviewService;
import kr.co.kosmo.project_back.user.dto.UserDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users/")
@RequiredArgsConstructor
public class UserReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{userId}/reviews")
    public ResponseEntity<List<MyReviewResponseDto>> getMyReviews(
        @PathVariable Long userId,
        HttpSession session
    ) {
        // 로그인 체크
        Integer loginUserId = (Integer) session.getAttribute("LOGIN_USER");
        if(loginUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 내 리뷰 조회
        List<MyReviewResponseDto> reviews = 
            reviewService.getMyReviews(loginUserId);
        return ResponseEntity.ok(reviews);
    }
}


// 내 리뷰 조회