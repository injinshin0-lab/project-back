package kr.co.kosmo.project_back.question.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.question.dto.QuestionRequestDto;
import kr.co.kosmo.project_back.question.dto.QuestionResponseDto;
import kr.co.kosmo.project_back.question.service.QuestionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users/inquiries")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    // 내 문의 목록 조회
    @GetMapping
    public ResponseEntity<List<QuestionResponseDto>> getQuestions(
            HttpSession session,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type
    ) {
        Integer userId = (Integer) session.getAttribute("LOGIN_USER");    
        if(userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<QuestionResponseDto> result = questionService.getQuestionList(userId, status, type);
        return ResponseEntity.ok(result);
    }

    // 문의 작성
    @PostMapping
    public ResponseEntity<QuestionResponseDto> insertQuestion(
        @ModelAttribute QuestionRequestDto dto,
        HttpSession session
    ) {
        Integer userId = (Integer) session.getAttribute("LOGIN_USER");
            if(userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            questionService.insertQuestion(userId, dto);
            return ResponseEntity.status(HttpStatus.CREATED).build(); 
    }
}
