package kr.co.kosmo.project_back.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchDto {
    private Integer userId;
    private Integer page;
    private Integer size;
    private String keyword;
    private String sort;
    private String order;
    
    public int getOffset() {
        return (page - 1) * size;
    }
}

// 입력용 DTO (상품정보 X)