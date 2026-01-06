package kr.co.kosmo.project_back.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
    private Integer id;
    private String categoryName;
    private Integer parentId;
    private Integer depth;
}
