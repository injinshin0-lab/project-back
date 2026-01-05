package kr.co.kosmo.project_back.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SearchMapper {
    List<String> findPopularKeywords();     // 인기 검색어 조회
}
