package kr.co.kosmo.project_back.admin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import kr.co.kosmo.project_back.admin.dto.QuestionAnswerDto;
import kr.co.kosmo.project_back.admin.dto.QuestionResponseDto;
import kr.co.kosmo.project_back.admin.dto.QuestionSearchDto;

@Mapper
public interface AdminQuestionMapper {
    Integer countQuestionList(QuestionSearchDto searchdto);
    List<QuestionResponseDto> findQuestionList(QuestionSearchDto searchdto);
    QuestionResponseDto findById(Integer id);
    Integer insertAnswer(QuestionAnswerDto dto);
    Integer updateQuestionStatus(Integer questionId);
    QuestionAnswerDto findAnswerByQuestionId(Integer questionId);
}







