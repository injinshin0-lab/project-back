package kr.co.kosmo.project_back.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import kr.co.kosmo.project_back.admin.dto.FaqDto;
import kr.co.kosmo.project_back.admin.dto.FaqSearchDto;
import java.util.List;

@Mapper
public interface AdminFaqMapper {
    Integer countFaqList(FaqSearchDto searchDto);
    List<FaqDto> findFaqList(FaqSearchDto searchDto);
    FaqDto findByFaqId(Integer faqId);
    Integer insertFaq(FaqDto dto);
    Integer updateFaq(FaqDto dto);
    Integer deleteFaq(Integer faqId);
}







