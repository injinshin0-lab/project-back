package kr.co.kosmo.project_back.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FaqSearchDto {
    private String keyword;
    private Integer page = 1;
    private Integer size = 10;
    
    public Integer getOffset() {
        if (page == null || size == null) {
            return 0;
        }
        return (page - 1) * size;
    }
}

