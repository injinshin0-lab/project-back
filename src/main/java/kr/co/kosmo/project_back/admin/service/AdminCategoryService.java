package kr.co.kosmo.project_back.admin.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.CategoryDto;
import kr.co.kosmo.project_back.admin.mapper.AdminCategoryMapper;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {
    private final AdminCategoryMapper categoryMapper;

    public List<CategoryDto> getCategories() {
        return categoryMapper.findAllCategories();
    }

    public Integer insertCategory(CategoryDto dto) {
        return categoryMapper.insertCategory(dto);
    }

    public Integer updateCategory(CategoryDto dto) {
        return categoryMapper.updateCategory(dto);
    }

    public Integer deleteCategory(Integer id) {
        return categoryMapper.deleteCategory(id);
    }
}







