package kr.co.kosmo.project_back.user.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.user.dto.QuestionResponseDto;
import kr.co.kosmo.project_back.user.dto.QuestionRequestDto;
import kr.co.kosmo.project_back.user.service.QuestionService;

@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<List<QuestionResponseDto>> getQuestionList(@RequestParam Integer userId) {
        return ResponseEntity.ok(questionService.getQuestionList(userId));
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionResponseDto> getQuestion(@PathVariable Integer questionId) {
        return ResponseEntity.ok(questionService.getQuestion(questionId));
    }

    @PostMapping
    public ResponseEntity<Integer> insertQuestion(
            @RequestParam Integer userId,
            @RequestParam Integer productId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String type,
            @RequestParam(required = false) List<MultipartFile> images) {
        QuestionRequestDto dto = new QuestionRequestDto();
        dto.setUserId(userId);
        dto.setProductId(productId);
        dto.setTitle(title);
        dto.setContent(content);
        dto.setType(type);
        dto.setImages(images);
        return ResponseEntity.ok(questionService.insertQuestion(dto));
    }
}

