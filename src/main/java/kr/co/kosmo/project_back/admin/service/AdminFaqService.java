package kr.co.kosmo.project_back.admin.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.FaqDto;
import kr.co.kosmo.project_back.admin.dto.FaqSearchDto;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.admin.mapper.AdminFaqMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminFaqService {
    private final AdminFaqMapper faqMapper;

    public PageResponseDto<FaqDto> getFaqList(FaqSearchDto searchDto) {
        Integer totalCount = faqMapper.countFaqList(searchDto);
        Integer totalPage = totalCount > 0 ? (int) Math.ceil((double) totalCount / searchDto.getSize()) : 1;
        
        PageResponseDto<FaqDto> response = new PageResponseDto<>();
        response.setList(faqMapper.findFaqList(searchDto));
        response.setTotalPage(totalPage);
        response.setCurrentPage(searchDto.getPage());
        
        return response;
    }

    public FaqDto getFaq(Integer faqId) {
        return faqMapper.findByFaqId(faqId);
    }

    public Integer insertFaq(FaqDto dto) {
        int result = faqMapper.insertFaq(dto);
        return result > 0 ? 1 : 0;
    }

    public Integer updateFaq(FaqDto dto) {
        int result = faqMapper.updateFaq(dto);
        return result > 0 ? 1 : 0;
    }

    public Integer deleteFaq(Integer faqId) {
        int result = faqMapper.deleteFaq(faqId);
        return result > 0 ? 1 : 0;
    }
}







