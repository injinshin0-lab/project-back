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
        if (dto.getParentId() == null) {
            dto.setDepth(1); // 최상위는 depth 1
        } else {
            // 부모의 정보를 조회해서 부모 depth + 1을 세팅하는 로직이 필요함
            // (현재는 간단히 처리하기 위해 프론트에서 depth를 넘겨받는 방식 사용)
        }
        return categoryMapper.insertCategory(dto);
    }

    public Integer updateCategory(CategoryDto dto) {
        return categoryMapper.updateCategory(dto);
    }

    public Integer deleteCategory(Integer id) {
        return categoryMapper.deleteCategory(id);
    }
}







