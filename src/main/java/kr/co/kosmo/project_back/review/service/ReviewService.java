package kr.co.kosmo.project_back.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.review.dto.request.ReviewRequestDto;
import kr.co.kosmo.project_back.review.dto.response.ReviewResponseDto;
import kr.co.kosmo.project_back.review.mapper.ReviewImageMapper;
import kr.co.kosmo.project_back.review.mapper.ReviewMapper;
import kr.co.kosmo.project_back.review.vo.ReviewVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper reviewMapper;
    private final ReviewImageMapper reviewImageMapper;

    // 상품별 리뷰 목록 조회
    public List<ReviewResponseDto> getReviewsByProduct(Integer productId, String sort) {
        return reviewMapper.findReviewsByProductId(productId, sort);
    }

    // 상품 리뷰 작성
    public Long insertReview(Integer productId, ReviewRequestDto dto) {
        List<String> images = dto.getImages();
        if(images != null && images.size() > 5) {
            throw new IllegalArgumentException("리뷰 이미지는 최대 5장까지 첨부 가능합니다.");
        }
        ReviewVO review = new ReviewVO();
        review.setUserId(1L);  // 임시처리
        review.setProductId(productId);
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());
        reviewMapper.insertReview(review);
        Long reviewId = review.getReviewId();

        // 이미지 저장
        if(images != null) {
            for(String imageUrl : images) {
                reviewImageMapper.insertReviewImage(reviewId, imageUrl);
            }
        }
        return reviewId;
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