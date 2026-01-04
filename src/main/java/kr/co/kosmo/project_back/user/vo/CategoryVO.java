package kr.co.kosmo.project_back.user.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryVO {
    private Integer id;
    private String categoryName;
    private Integer parentId;
    private Integer depth;
}
