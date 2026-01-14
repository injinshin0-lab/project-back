package kr.co.kosmo.project_back.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserSelectedCategoryMapper {
    void insertUserCategories(      // 회원가입 시 선택한 관심분야 저장. 향후 추천 기능에 사용예정
        @Param("userId") Integer userId,
        @Param("categoryIds") List<Integer> categoryIds);
        List<Integer> findUserCategory(@Param("userId") Integer userId);
        void deleteCategoriesByUser(@Param("userId") Integer userId);

    

}
