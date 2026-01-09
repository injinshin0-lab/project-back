package kr.co.kosmo.project_back.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.admin.dto.QuestionAnswerDto;
import kr.co.kosmo.project_back.admin.dto.QuestionResponseDto;
import kr.co.kosmo.project_back.admin.dto.QuestionSearchDto;
import kr.co.kosmo.project_back.admin.service.AdminQuestionService;

@RestController
@RequestMapping("/api/v1/admin/inquiries")
@RequiredArgsConstructor
public class AdminQuestionController {
    private final AdminQuestionService questionService;

    @GetMapping
    public ResponseEntity<PageResponseDto<QuestionResponseDto>> getQuestionList(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String questionStatus,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keywordType,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        QuestionSearchDto searchDto = new QuestionSearchDto();
        searchDto.setStartDate(startDate);
        searchDto.setEndDate(endDate);
        searchDto.setQuestionStatus(questionStatus);
        searchDto.setType(type);
        searchDto.setKeywordType(keywordType);
        searchDto.setKeyword(keyword);
        searchDto.setPage(page);
        searchDto.setSize(size);
        
        return ResponseEntity.ok(questionService.getQuestionList(searchDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> getQuestion(@PathVariable Integer id) {
        return ResponseEntity.ok(questionService.getQuestion(id));
    }

    @PatchMapping("/{inquiriesId}/reply")
    public ResponseEntity<Integer> insertAnswer(
        @PathVariable("inquiriesId") Integer inquiriesId,
        @RequestBody QuestionAnswerDto dto
    ) {
        dto.setQuestionid(inquiriesId);
        
        return ResponseEntity.ok(questionService.insertAnswer(dto));
    }
}

