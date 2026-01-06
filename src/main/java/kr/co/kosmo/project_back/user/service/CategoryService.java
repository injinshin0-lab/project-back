package kr.co.kosmo.project_back.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.user.dto.CategoryDto;
import kr.co.kosmo.project_back.user.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryMapper categoryMapper;

    // 회원가입 화면용 카테고리 조회
    public List<CategoryDto> getCategories() {
        return categoryMapper.findCategories();
    }
}
