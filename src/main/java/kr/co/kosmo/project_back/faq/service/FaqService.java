package kr.co.kosmo.project_back.faq.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.faq.dto.FaqDto;
import kr.co.kosmo.project_back.faq.dto.FaqSearchDto;
import kr.co.kosmo.project_back.faq.mapper.FaqMapper;
import kr.co.kosmo.project_back.product.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqService {
    private final FaqMapper faqMapper;

    // faq 목록 조회(검색 + 페이징)
    public PageResponseDto<FaqDto> getFaqList(FaqSearchDto searchDto) {
        // faq 전체 개수 조회
        int totalCount = faqMapper.countFaqList(searchDto);
        // faq 목록 조회
        List<FaqDto> list = faqMapper.findFaqList(searchDto);
        // 전체 페이지 수 계산
        return PageResponseDto.of(list, totalCount, searchDto.getPage(), searchDto.getSize());
    }
    // faq 단건 조회 
    public FaqDto getFaq(Integer faqId) {
        return faqMapper.findByFaqId(faqId);
    }
}