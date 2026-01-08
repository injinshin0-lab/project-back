package kr.co.kosmo.project_back.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.kosmo.project_back.user.dto.QuestionResponseDto;
import kr.co.kosmo.project_back.user.dto.QuestionRequestDto;
import kr.co.kosmo.project_back.user.dto.QuestionAnswerDto;

@Mapper
public interface QuestionMapper {
    List<QuestionResponseDto> findQuestionByUserId(@Param("userId") Integer userId);
    QuestionResponseDto findByQuestionId(@Param("questionId") Integer questionId);
    Integer insertQuestion(QuestionRequestDto dto);
    QuestionAnswerDto findAnswerByQuestionId(@Param("questionId") Integer questionId);
}







