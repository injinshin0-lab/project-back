package kr.co.kosmo.project_back.faq.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqSearchDto {
    private String keyword;
    private Integer page;
    private Integer size;

    public Integer getOffset() {
        if(page == null || size == null) {
            return 0;
        }
        return (page - 1) * size;
    }
}