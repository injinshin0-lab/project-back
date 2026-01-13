package kr.co.kosmo.project_back.faq.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.kosmo.project_back.faq.dto.FaqDto;
import kr.co.kosmo.project_back.faq.dto.FaqSearchDto;

@Mapper
public interface FaqMapper {
    // faq 목록 조회 (검색 + 페이징)
    List<FaqDto> findFaqList(FaqSearchDto searchDto);
    // faq 전체 개수 (페이징)
    int countFaqList(FaqSearchDto searchDto);
    // faq 단건 조회
    FaqDto findByFaqId(Integer faqId);
}