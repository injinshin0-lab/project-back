package kr.co.kosmo.project_back.admin.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchDto {
    private Integer userId;
    private Integer page = 1;
    private Integer size = 10;
    private String keyword;
    private String sort;
    private String order;
    private Integer categoryId;
    private List<Integer> categoryIds; // 하위 카테고리 포함 검색용
    private Integer offset; // 페이징용 offset

    public Integer getOffset() {
        if (offset != null) {
            return offset;
        }
        if (page == null || size == null) {
            return 0;
        }
        return (page - 1) * size;
    }
    
    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
