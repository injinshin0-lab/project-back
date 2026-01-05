package kr.co.kosmo.project_back.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.review.dto.request.ReviewRequestDto;
import kr.co.kosmo.project_back.review.dto.response.ReviewResponseDto;
import kr.co.kosmo.project_back.review.mapper.ReviewMapper;
import kr.co.kosmo.project_back.review.vo.ReviewVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper reviewMapper;

    // 상품별 리뷰 목록 조회
    public List<ReviewResponseDto> getReviewsByProduct(Integer productId, String sort) {
        return reviewMapper.findReviewsByProductId(productId, sort);
    }

    // 상품 리뷰 작성
    public Long insertReview(Integer productId, ReviewRequestDto dto) {
    
        ReviewVO review = new ReviewVO();
        review.setUserId(1L);  // 임시처리
        review.setProductId(productId);
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());
        reviewMapper.insertReview(review);
        return review.getReviewId();
    }

    // 상품 리뷰 수정
    public void updateReview(Long reviewId, ReviewRequestDto dto) {
        ReviewVO review = new ReviewVO();
        review.setReviewId(reviewId);
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());
        reviewMapper.updateReview(review);
    }

    // 상품 리뷰 삭제
    public void deleteReview(Long reviewId) {
        reviewMapper.deleteReview(reviewId);
    }
}