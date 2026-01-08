package kr.co.kosmo.project_back.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.FaqDto;
import kr.co.kosmo.project_back.admin.dto.FaqSearchDto;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.user.service.FaqService;

@RestController
@RequestMapping("/api/v1/faq")
@RequiredArgsConstructor
public class FaqController {
    
    private final FaqService faqService;

    @GetMapping
    public ResponseEntity<PageResponseDto<FaqDto>> getFaqList(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        FaqSearchDto searchDto = new FaqSearchDto();
        searchDto.setKeyword(keyword);
        searchDto.setPage(page);
        searchDto.setSize(size);
        
        return ResponseEntity.ok(faqService.getFaqList(searchDto));
    }

    @GetMapping("/{faqId}")
    public ResponseEntity<FaqDto> getFaq(@PathVariable Integer faqId) {
        return ResponseEntity.ok(faqService.getFaq(faqId));
    }
}

