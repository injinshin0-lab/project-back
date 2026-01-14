package kr.co.kosmo.project_back.review.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.kosmo.project_back.review.dto.request.ReviewRequestDto;
import kr.co.kosmo.project_back.review.dto.response.MyReviewResponseDto;
import kr.co.kosmo.project_back.review.dto.response.ReviewResponseDto;

@Mapper
public interface ReviewMapper {
    // 상품별 리뷰 목록 조회
   List<ReviewResponseDto> findReviewsByProductId(
        @Param("productId") Integer productId,
        @Param("sort") String sort 
   );
   
   // 상품 리뷰 작성
   void insertReview(ReviewRequestDto review);

   // 상품 리뷰 수정
   int updateReview(ReviewRequestDto review);

   // 상품 리뷰 삭제
   int deleteReview(@Param("reviewId") Long reviewId);

   // 마이페이지 내 리뷰 조회
   List<MyReviewResponseDto> findReviewsByUserId(Integer userId);
}
