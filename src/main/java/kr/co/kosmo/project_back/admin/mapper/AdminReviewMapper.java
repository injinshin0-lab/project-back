package kr.co.kosmo.project_back.admin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import kr.co.kosmo.project_back.admin.dto.AdminReviewResponseDto;

@Mapper
public interface AdminReviewMapper {
    List<AdminReviewResponseDto> findReviewsByProductId(Integer productId);
    AdminReviewResponseDto findByReviewId(Integer reviewId);
    Integer deleteReviewByReviewId(Integer reviewId);
}


