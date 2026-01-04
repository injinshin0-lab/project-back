package kr.co.kosmo.project_back.review.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.kosmo.project_back.review.dto.response.ReviewResponseDto;
import kr.co.kosmo.project_back.review.vo.ReviewVO;

@Mapper
public interface ReviewMapper {
    // 상품별 리뷰 목록 조회
   List<ReviewResponseDto> findReviewsByProductId(
        @Param("productId") Integer productId,
        @Param("sort") String sort 
   );
   
   // 상품 리뷰 작성
   void insertReview(ReviewVO review);

   // 상품 리뷰 수정
   int updateReview(ReviewVO review);

   // 상품 리뷰 삭제
   int deleteReview(Long reviewId);
}
