package kr.co.kosmo.project_back.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.kosmo.project_back.user.dto.ReviewResponseDto;
import kr.co.kosmo.project_back.user.dto.ReviewRequestDto;

@Mapper
public interface ReviewMapper {
    List<ReviewResponseDto> findReviewsByProductId(Integer productId);
    List<ReviewResponseDto> findAllReviews();
    List<ReviewResponseDto> findReviewsByUserId(Integer userId);
    ReviewResponseDto findByReviewId(Integer reviewId);
    Integer insertReview(ReviewRequestDto dto);
    Boolean hasDeliveredOrder(@Param("userId") Integer userId, @Param("productId") Integer productId);
    Boolean hasExistingReview(@Param("userId") Integer userId, @Param("productId") Integer productId);
}




