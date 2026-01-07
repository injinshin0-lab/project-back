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
    private String sort;    // 최신순, 가격, 별점, 인기
    private String order;   // 오름차순, 내림차순
    
    public int getOffset() {
        return (page - 1) * size;
    } // 이 페이지를 보여주기 위해 몇개를 건너뛰어야하는지 구하기 위함
}

// 입력용 DTO (상품정보 X)

