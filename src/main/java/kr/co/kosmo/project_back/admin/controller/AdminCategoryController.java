package kr.co.kosmo.project_back.admin.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.CategoryDto;
import kr.co.kosmo.project_back.admin.service.AdminCategoryService;

@RestController
@RequestMapping("/api/v1/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final AdminCategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @PostMapping
    public ResponseEntity<Integer> insertCategory(@RequestBody CategoryDto dto) {
        return ResponseEntity.ok(categoryService.insertCategory(dto));
    }

    @PutMapping
    public ResponseEntity<Integer> updateCategory(@RequestBody CategoryDto dto) {
        return ResponseEntity.ok(categoryService.updateCategory(dto));
    }

    @DeleteMapping
    public ResponseEntity<Integer> deleteCategory(@RequestParam Integer id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}







