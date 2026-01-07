package kr.co.kosmo.project_back.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.AdminProductRequestDto;
import kr.co.kosmo.project_back.admin.dto.AdminProductResponseDto;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.admin.dto.ProductSearchDto;
import kr.co.kosmo.project_back.admin.service.AdminProductService;

@RestController
@RequestMapping("/api/v1/admin/product")
@RequiredArgsConstructor
public class AdminProductController {
    private final AdminProductService adminProductService;

    @GetMapping
    public ResponseEntity<PageResponseDto<AdminProductResponseDto>> getProductList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setKeyword(keyword);
        searchDto.setSort(sort);
        searchDto.setOrder(order);
        searchDto.setCategoryId(categoryId);
        searchDto.setPage(page);
        searchDto.setSize(size);
        
        return ResponseEntity.ok(adminProductService.getProductList(searchDto));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<AdminProductResponseDto> getProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(adminProductService.getProduct(productId));
    }

    @PostMapping
    public ResponseEntity<Integer> insertProduct(@ModelAttribute AdminProductRequestDto dto) {
        return ResponseEntity.ok(adminProductService.insertProduct(dto));
    }

    @PutMapping
    public ResponseEntity<Integer> updateProduct(@ModelAttribute AdminProductRequestDto dto) {
        return ResponseEntity.ok(adminProductService.updateProduct(dto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Integer> deleteProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(adminProductService.deleteProduct(productId));
    }
}


