package kr.co.kosmo.project_back.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserSelectedCategoryMapper {
    void insertUserCategories(
        @Param("userId") Integer userId,
        @Param("categoryIds") List<Integer> categoryIds);
        List<Integer> findUserCategory(Integer userId);
        void deleteCategoriesByUser(Integer userId);
}
