package kr.co.kosmo.project_back.faq.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.faq.dto.FaqDto;
import kr.co.kosmo.project_back.faq.dto.FaqSearchDto;
import kr.co.kosmo.project_back.faq.service.FaqService;
import kr.co.kosmo.project_back.product.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/faqs")
@RequiredArgsConstructor
public class FaqController {
   private final FaqService faqService;

   // faq 목록 조회 (검색 + 페이징)
   @GetMapping
   public PageResponseDto<FaqDto> getFaqList(
            @ModelAttribute FaqSearchDto searchDto,HttpSession session) {
        if(searchDto.getPage() == null || searchDto.getPage() <= 0) {
            searchDto.setPage(1);
        }
        if(searchDto.getSize() == null || searchDto.getSize() <= 0) {
            searchDto.setSize(10);
        }
        return faqService.getFaqList(searchDto);
    }
}
