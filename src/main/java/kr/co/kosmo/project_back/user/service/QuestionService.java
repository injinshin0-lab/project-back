package kr.co.kosmo.project_back.user.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.user.dto.QuestionResponseDto;
import kr.co.kosmo.project_back.user.dto.QuestionRequestDto;
import kr.co.kosmo.project_back.user.dto.QuestionAnswerDto;
import kr.co.kosmo.project_back.user.mapper.QuestionMapper;
import kr.co.kosmo.project_back.user.mapper.QuestionImageMapper;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionMapper questionMapper;
    private final QuestionImageMapper questionImageMapper;

    // 파일 업로드 기본 경로 (리뷰/상품과 동일 설정 사용)
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public List<QuestionResponseDto> getQuestionList(Integer userId) {
        List<QuestionResponseDto> questions = questionMapper.findQuestionByUserId(userId);
        for (QuestionResponseDto question : questions) {
            question.setQuestionImages(questionImageMapper.findImagesByQuestionId(question.getQuestionId()));
        }
        return questions;
    }

    public QuestionResponseDto getQuestion(Integer questionId) {
        QuestionResponseDto question = questionMapper.findByQuestionId(questionId);
        if (question != null) {
            question.setQuestionImages(questionImageMapper.findImagesByQuestionId(questionId));
            
            // 답변 조회
            QuestionAnswerDto answer = questionMapper.findAnswerByQuestionId(questionId);
            question.setAnswer(answer);
        }
        return question;
    }

    public Integer insertQuestion(QuestionRequestDto dto) {
        Integer result = questionMapper.insertQuestion(dto);
        if (result > 0 && dto.getImages() != null && !dto.getImages().isEmpty()) {
            Integer questionId = dto.getQuestionId();
            for (MultipartFile image : dto.getImages()) {
                if (image != null && !image.isEmpty()) {
                    try {
                        String imageUrl = saveQuestionImageFile(image);
                        questionImageMapper.insertQuestionImage(questionId, imageUrl);
                    } catch (RuntimeException e) {
                        // 파일 저장 실패 시 문의 자체는 살리고 로그만 남기는 용도로 사용 가능
                        // 여기서는 조용히 무시 (필요시 로깅 추가)
                    }
                }
            }
        }
        return result;
    }

    /**
     * 문의 이미지를 파일로 저장하고, 프론트에서 접근 가능한 URL을 반환
     * (예: /uploads/question/파일명)
     */
    private String saveQuestionImageFile(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(uploadDir, "question");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String filename = UUID.randomUUID().toString() + extension;

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            // WebConfig 에서 /uploads/** -> file:uploads/ 로 매핑되어 있으므로,
            // 하위 경로를 포함한 URL을 반환
            return "/uploads/question/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("문의 이미지 파일 저장 실패: " + e.getMessage(), e);
        }
    }
}



