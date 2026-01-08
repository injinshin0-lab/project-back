package kr.co.kosmo.project_back.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.kosmo.project_back.user.dto.QuestionImageDto;

@Mapper
public interface QuestionImageMapper {
    List<QuestionImageDto> findImagesByQuestionId(@Param("questionId") Integer questionId);
    Integer insertQuestionImage(@Param("questionId") Integer questionId, @Param("imageUrl") String imageUrl);
}







