package kr.co.kosmo.project_back.question.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.kosmo.project_back.question.dto.QuestionRequestDto;
import kr.co.kosmo.project_back.question.dto.QuestionResponseDto;

@Mapper
public interface QuestionMapper {
    // 1:1 문의 조회
    List<QuestionResponseDto> findQuestionByUserId(
        @Param("userId") Integer userId,
        @Param("status") String status,
        @Param("type") String type);
    // 1:1 문의 작성
    void insertQuestion(QuestionRequestDto question);




}
