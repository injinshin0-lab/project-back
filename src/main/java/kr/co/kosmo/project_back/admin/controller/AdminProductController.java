package kr.co.kosmo.project_back.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.AdminProductRequestDto;
import kr.co.kosmo.project_back.admin.dto.AdminProductResponseDto;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.admin.dto.ProductSearchDto;
import kr.co.kosmo.project_back.admin.service.AdminProductService;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class AdminProductController {
    private final AdminProductService adminProductService;

    @GetMapping
    public ResponseEntity<PageResponseDto<AdminProductResponseDto>> getProductList(
        @ModelAttribute ProductSearchDto searchDto) {
    
    System.out.println("검색어: " + searchDto.getKeyword());
    System.out.println("카테고리 리스트: " + searchDto.getCategoryIds());
    
    return ResponseEntity.ok(adminProductService.getProductList(searchDto));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<AdminProductResponseDto> getProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(adminProductService.getProduct(productId));
    }

    @PostMapping
    public ResponseEntity<Integer> insertProduct(@ModelAttribute AdminProductRequestDto dto) {

        System.out.println("전달받은 상품명: " + dto.getProductName());
        System.out.println("전달받은 카테고리 리스트: " + dto.getCategoryIds());
        return ResponseEntity.ok(adminProductService.insertProduct(dto));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<Integer> updateProduct(
        @PathVariable("productId") Integer productId, 
        @ModelAttribute AdminProductRequestDto dto
    ) {
        // URL로 넘어온 ID를 DTO에 강제로 세팅해주는 것이 안전합니다.
        dto.setProductId(productId);
        return ResponseEntity.ok(adminProductService.updateProduct(dto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Integer> deleteProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(adminProductService.deleteProduct(productId));
    }
}


