package kr.co.kosmo.project_back.admin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import kr.co.kosmo.project_back.admin.dto.QuestionImageDto;

@Mapper
public interface AdminQuestionImageMapper {
    List<QuestionImageDto> findImagesByQuestionId(Integer questionId);
    Integer insertQuestionImage(Integer questionId, String imageUrl);
}







