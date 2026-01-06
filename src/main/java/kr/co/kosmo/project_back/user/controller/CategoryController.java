package kr.co.kosmo.project_back.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.kosmo.project_back.user.service.CategoryService;
import kr.co.kosmo.project_back.user.vo.CategoryVO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryVO>> getCatgegories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }
}
