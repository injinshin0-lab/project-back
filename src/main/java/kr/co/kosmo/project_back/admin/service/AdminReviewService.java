package kr.co.kosmo.project_back.admin.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.AdminReviewResponseDto;
import kr.co.kosmo.project_back.admin.mapper.AdminReviewImageMapper;
import kr.co.kosmo.project_back.admin.mapper.AdminReviewMapper;

@Service
@RequiredArgsConstructor
public class AdminReviewService {
    private final AdminReviewMapper adminReviewMapper;
    private final AdminReviewImageMapper adminReviewImageMapper;

    public List<AdminReviewResponseDto> getReviewsByProduct(Integer productId) {
        List<AdminReviewResponseDto> reviews = adminReviewMapper.findReviewsByProductId(productId);
        for (AdminReviewResponseDto review : reviews) {
            review.setReviewImages(adminReviewImageMapper.findImagesByReviewId(review.getReviewId()));
        }
        return reviews;
    }

    public AdminReviewResponseDto getReview(Integer reviewId) {
        AdminReviewResponseDto review = adminReviewMapper.findByReviewId(reviewId);
        if (review != null) {
            review.setReviewImages(adminReviewImageMapper.findImagesByReviewId(reviewId));
        }
        return review;
    }

    public Integer deleteReview(Integer reviewId) {
        // 리뷰 이미지 먼저 삭제
        adminReviewImageMapper.deleteImagesByReviewId(reviewId);
        // 리뷰 삭제
        int result = adminReviewMapper.deleteReviewByReviewId(reviewId);
        return result > 0 ? 1 : 0;
    }
}


