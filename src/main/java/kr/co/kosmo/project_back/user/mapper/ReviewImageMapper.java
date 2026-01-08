package kr.co.kosmo.project_back.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.kosmo.project_back.user.dto.ReviewImageDto;

@Mapper
public interface ReviewImageMapper {
    List<ReviewImageDto> findByReviewId(Integer reviewId);
    Integer insertReviewImage(@Param("reviewId") Integer reviewId, @Param("imageUrl") String imageUrl);
}








