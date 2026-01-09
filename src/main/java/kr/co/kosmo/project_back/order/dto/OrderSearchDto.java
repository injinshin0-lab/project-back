package kr.co.kosmo.project_back.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchDto {
    private Integer userId;
    private String startDate;
    private String endDate;
    private Integer page;
    private Integer size;
    public int getOffset() {       // 페이징처리
        return (page -1) * size;
    }
}
