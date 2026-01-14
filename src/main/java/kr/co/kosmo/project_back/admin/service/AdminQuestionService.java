package kr.co.kosmo.project_back.admin.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.admin.dto.QuestionAnswerDto;
import kr.co.kosmo.project_back.admin.dto.QuestionResponseDto;
import kr.co.kosmo.project_back.admin.dto.QuestionSearchDto;
import kr.co.kosmo.project_back.admin.mapper.AdminQuestionMapper;
import kr.co.kosmo.project_back.admin.mapper.AdminQuestionImageMapper;
import kr.co.kosmo.project_back.admin.service.AdminAlarmService;

@Service
@RequiredArgsConstructor
public class AdminQuestionService {
    private final AdminQuestionMapper questionMapper;
    private final AdminQuestionImageMapper questionImageMapper;
    private final AdminAlarmService alarmService;

    public PageResponseDto<QuestionResponseDto> getQuestionList(QuestionSearchDto searchDto) {
        Integer totalCount = questionMapper.countQuestionList(searchDto);
        Integer totalPage = (int) Math.ceil((double) totalCount / searchDto.getSize());
        
        List<QuestionResponseDto> questionList = questionMapper.findQuestionList(searchDto);
        
        // 각 질문에 이미지 리스트 추가
        for (QuestionResponseDto question : questionList) {
            List<kr.co.kosmo.project_back.admin.dto.QuestionImageDto> images = 
                questionImageMapper.findImagesByQuestionId(question.getId());
            question.setQuestionImages(images);
        }
        
        PageResponseDto<QuestionResponseDto> response = new PageResponseDto<>();
        response.setList(questionList);
        response.setTotalPage(totalPage);
        response.setCurrentPage(searchDto.getPage());
        
        return response;
    }

    public QuestionResponseDto getQuestion(Integer id) {
        QuestionResponseDto question = questionMapper.findById(id);
        if (question != null) {
            List<kr.co.kosmo.project_back.admin.dto.QuestionImageDto> images = 
                questionImageMapper.findImagesByQuestionId(id);
            question.setQuestionImages(images);
            
            // 답변 조회
            QuestionAnswerDto answer = questionMapper.findAnswerByQuestionId(id);
            question.setAnswer(answer);
        }
        return question;
    }

    public Integer insertAnswer(QuestionAnswerDto dto) {
        Integer result = questionMapper.insertAnswer(dto);
        if (result > 0) {
            questionMapper.updateQuestionStatus(dto.getQuestionid());
            
            // 문의 정보 조회하여 사용자 ID 확인
            QuestionResponseDto question = questionMapper.findById(dto.getQuestionid());
            if (question != null && question.getUserId() != null) {
                // 알림 발송
                try {
                    alarmService.insertAnswerAlarm(question.getUserId(), question.getTitle());
                } catch (Exception e) {
                    // 알림 발송 실패해도 답변 등록은 성공으로 처리
                    System.err.println("알림 발송 실패: " + e.getMessage());
                }
            }
        }
        return result;
    }
}







