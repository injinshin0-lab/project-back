package kr.co.kosmo.project_back.question.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.kosmo.project_back.admin.dto.QuestionAnswerDto;
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

    // 회원 탈퇴
    // 사용자가 작성한 문의 목록 조회
    List<Long> findQuestionIdsByUserId(@Param("userId") Integer userId);
    // 사용자가 작성한 문의 목록 삭제
    void deleteByUserId(@Param("userId") Integer userId);

    List<Map<String, Object>> searchProductOrOrder(Integer userId, String keyword);
    QuestionAnswerDto findAnswerByQuestionId(@Param("questionId") Integer questionId);
}


