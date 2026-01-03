package kr.co.kosmo.project_back.review.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.kosmo.project_back.review.dto.response.ReviewResponseDto;

@Mapper
public interface ReviewMapper {
    // 상품별 리뷰 목록 조회
   List<ReviewResponseDto> findReviewsByProductId(
        @Param("productId") Integer productId,
        @Param("sort") String sort 
   );
   
}
