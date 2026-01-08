package kr.co.kosmo.project_back.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SearchMapper {
    Integer insertSearchHistory(@Param("userId") Integer userId, @Param("keyword") String keyword);
    List<String> findPopularKeywords();
    List<String> findRecentKeywordsByUserId(Integer userId);
}

