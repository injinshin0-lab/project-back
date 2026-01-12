package kr.co.kosmo.project_back.question.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.kosmo.project_back.question.dto.QuestionImageDto;

@Mapper
public interface QuestionImageMapper {
    List<QuestionImageDto> findImagesByQuestionId
        (@Param("questionId") Integer questionId);
    void insertQuestionImage(
        @Param("questionId") Integer questionId,
        @Param("imageUrl") String imageUrl 
    );
}
