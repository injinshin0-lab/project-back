package kr.co.kosmo.project_back.review.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewImageMapper {
    void insertReviewImage(
        @Param("reviewId") Long reviewId,
        @Param("imageUrl") String imageUrl 
    );  
}
