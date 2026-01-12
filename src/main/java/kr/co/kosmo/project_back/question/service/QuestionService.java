package kr.co.kosmo.project_back.question.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.kosmo.project_back.question.dto.QuestionRequestDto;
import kr.co.kosmo.project_back.question.dto.QuestionResponseDto;
import kr.co.kosmo.project_back.question.mapper.QuestionImageMapper;
import kr.co.kosmo.project_back.question.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionMapper questionMapper;
    private final QuestionImageMapper questionImageMapper;

    // 내 문의 목록 조회
    public List<QuestionResponseDto> getQuestionList(
        Integer userId, String status, String type) {
        // 문의 목록 조회
        List<QuestionResponseDto> questions = 
            questionMapper.findQuestionByUserId(userId, status, type);
        // 각 문의에 이미지 붙이기
        for(QuestionResponseDto dto : questions) {
            dto.setQuestionImages(
                questionImageMapper.findImagesByQuestionId(dto.getQuestionId())
            );
        }
        return questions;
    }
    
    // 문의 작성
    public void insertQuestion(Integer userId, QuestionRequestDto dto) {
        dto.setUserId(userId);
        questionMapper.insertQuestion(dto);
        // 문의 저장
        Integer questionId = dto.getQuestionId();
        // 이미지 있다면 저장
        if(dto.getImages() != null && !dto.getImages().isEmpty()) {
            for(MultipartFile image : dto.getImages()) {
                String imageUrl = image.getOriginalFilename();
                questionImageMapper.insertQuestionImage(
                    questionId, imageUrl);
            }
        }
    }

}
