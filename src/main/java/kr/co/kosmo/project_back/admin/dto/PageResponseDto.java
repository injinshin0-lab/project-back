package kr.co.kosmo.project_back.admin.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto<T> {
    private List<T> list;
    private Integer totalPage;
    private Integer currentPage;
}
