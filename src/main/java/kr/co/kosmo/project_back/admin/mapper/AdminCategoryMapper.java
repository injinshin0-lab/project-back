package kr.co.kosmo.project_back.admin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.kosmo.project_back.admin.dto.CategoryDto;

@Mapper
public interface AdminCategoryMapper {
    List<CategoryDto> findAllCategories();
    Integer insertCategory(CategoryDto dto);
    Integer updateCategory(CategoryDto dto);
    Integer deleteCategory(Integer id);
    // 특정 카테고리의 모든 하위 카테고리 ID 목록 조회 (재귀)
    List<Integer> findDescendantCategoryIds(@Param("categoryId") Integer categoryId);
}


