package kr.co.kosmo.project_back.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchDto {
    private Integer userId;
    private String loginId; // 유저 아이디 검색
    private String productName; // 상품명 검색
    private String status; // 상태별 검색
    private String startDate;
    private String endDate;
    private String sort; // 정렬 컬럼 (totalPrice, createdAt)
    private String order; // 정렬 순서 (asc, desc)
    private Integer page = 1;
    private Integer size = 10;

    public Integer getOffset() {
        return (page - 1) * size;
    }
}
