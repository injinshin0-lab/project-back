package kr.co.kosmo.project_back.review.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.kosmo.project_back.review.dto.response.ReviewImageDto;

@Mapper
public interface ReviewImageMapper {
    void insertReviewImage(
        @Param("reviewId") Long reviewId,
        @Param("imageUrl") String imageUrl 
    );  
    List<ReviewImageDto> findByReviewId(@Param("reviewId") Long reviewId);
    // 탈퇴용
    void deleteByReviewId(@Param("reviewId")Long reviewId);
}
