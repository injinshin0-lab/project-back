package kr.co.kosmo.project_back.product.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class PageResponseDto<T> {
    private List<T> list;
    private int totalPage;
    private int currentPage;

    private PageResponseDto(List<T> list, int currentPage, int totalPage) {
        this.list = list;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }

    // 페이지 응답처리
    public static <T> PageResponseDto<T> of(
        List<T> list,
        int totalCount,
        int currentPage,
        int size
    ) {
        int totalPage = (int) Math.ceil((double) totalCount / size);
        return new PageResponseDto<>(list, currentPage, totalPage);
    }
}

// 현재 페이지에 보여줄 목록, 현재 페이지 번호, 전체 페이지수