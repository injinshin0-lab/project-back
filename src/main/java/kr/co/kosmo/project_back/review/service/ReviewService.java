package kr.co.kosmo.project_back.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.review.dto.response.ReviewResponseDto;
import kr.co.kosmo.project_back.review.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper reviewMapper;

    // 상품별 리뷰 목록 조회
    public List<ReviewResponseDto> getReviewsByProduct(Integer productId, String sort) {
        return reviewMapper.findReviewsByProductId(productId, sort);
    }
}
