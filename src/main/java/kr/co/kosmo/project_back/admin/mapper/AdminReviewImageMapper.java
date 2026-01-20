package kr.co.kosmo.project_back.admin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import kr.co.kosmo.project_back.admin.dto.AdminReviewImageDto;

@Mapper
public interface AdminReviewImageMapper {
    List<AdminReviewImageDto> findImagesByReviewId(Integer reviewId);
    Integer deleteImagesByReviewId(Integer reviewId);
}


