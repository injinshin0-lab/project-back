package kr.co.kosmo.project_back.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.kosmo.project_back.admin.dto.CategoryDto;

@Mapper
public interface SelectedCategoryMapper {
    List<CategoryDto> findUserCategory(@Param("userId") Integer userId);
    Integer deleteCategoriesByUserId(@Param("userId") Integer userId);
    Integer insertUserCategories(@Param("userId") Integer userId, @Param("categoryIds") List<Integer> categoryIds);
}







