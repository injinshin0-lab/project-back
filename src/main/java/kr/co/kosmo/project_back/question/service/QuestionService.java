package kr.co.kosmo.project_back.question.service;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.kosmo.project_back.alarm.service.AlarmService;
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
    private final AlarmService alarmService;

    // application.properties에 설정된 업로드 루트 경로
    @Value("${file.upload.review-path}") // 기존 설정을 그대로 쓰거나 새로 만드세요
    private String uploadPath;

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
        // [핵심] 리뷰 기능처럼 실제 파일 저장 로직 추가
        if(dto.getImages() != null && !dto.getImages().isEmpty()) {
            for(MultipartFile image : dto.getImages()) {
                if(!image.isEmpty()) {
                    String imageUrl = saveQuestionImage(image); // 실제 저장 및 URL 생성
                    questionImageMapper.insertQuestionImage(questionId, imageUrl);
                }
            }
        }
    }

    // [리뷰 서비스 로직 이식] 실제 파일 저장 메서드
    private String saveQuestionImage(MultipartFile file) {
        try {
            String original = file.getOriginalFilename();
            String ext = (original != null && original.contains(".")) 
                         ? original.substring(original.lastIndexOf(".")) : "";
            String fileName = UUID.randomUUID() + ext;

            // 날짜별 폴더 생성 (예: /inquiries/2026/01)
            String datePath = LocalDate.now().getYear() + "/" + String.format("%02d", LocalDate.now().getMonthValue());
            String dirPath = uploadPath + "inquiries/" + datePath;
            
            File dir = new File(dirPath);
            if(!dir.exists()) dir.mkdirs();

            // 파일 실제 저장
            File saveFile = new File(dir, fileName);
            file.transferTo(saveFile);

            // DB에 저장될 웹 접근 경로 리턴
            return "/uploads/inquiries/" + datePath + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("문의 이미지 저장 실패", e);
        }
    }


}
