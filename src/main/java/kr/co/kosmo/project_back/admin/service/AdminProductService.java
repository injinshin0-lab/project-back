package kr.co.kosmo.project_back.admin.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.AdminProductRequestDto;
import kr.co.kosmo.project_back.admin.dto.AdminProductResponseDto;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.admin.dto.ProductSearchDto;
import kr.co.kosmo.project_back.admin.mapper.AdminCategoryMapper;
import kr.co.kosmo.project_back.admin.mapper.AdminProductMapper;
import kr.co.kosmo.project_back.admin.mapper.CategoryProductMappingMapper;

@Service
@RequiredArgsConstructor
public class AdminProductService {
    private final AdminProductMapper adminProductMapper;
    private final AdminCategoryMapper adminCategoryMapper;
    private final CategoryProductMappingMapper categoryProductMappingMapper;
    
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public PageResponseDto<AdminProductResponseDto> getProductList(ProductSearchDto searchDto) {
        // 카테고리 ID가 있으면 하위 카테고리 포함하여 검색
        if (searchDto.getCategoryId() != null) {
            List<Integer> categoryIds = new ArrayList<>();
            categoryIds.add(searchDto.getCategoryId()); // 자기 자신도 포함
            
            // 하위 카테고리 ID 목록 조회
            List<Integer> descendantIds = adminCategoryMapper.findDescendantCategoryIds(searchDto.getCategoryId());
            if (descendantIds != null && !descendantIds.isEmpty()) {
                categoryIds.addAll(descendantIds);
            }
            
            searchDto.setCategoryIds(categoryIds);
        }

        // offset 계산
        Integer offset = (searchDto.getPage() - 1) * searchDto.getSize();
        searchDto.setOffset(offset);
        
        Integer totalCount = adminProductMapper.countProductList(searchDto);
        Integer totalPage = totalCount > 0 ? (int) Math.ceil((double) totalCount / searchDto.getSize()) : 1;
        
        // 디버깅: 총 개수와 페이지 정보 출력
        System.out.println("=== 검색 결과 디버깅 ===");
        System.out.println("Keyword: " + searchDto.getKeyword());
        System.out.println("CategoryId: " + searchDto.getCategoryId());
        System.out.println("CategoryIds: " + searchDto.getCategoryIds());
        System.out.println("TotalCount: " + totalCount);
        System.out.println("Size: " + searchDto.getSize());
        System.out.println("TotalPage: " + totalPage);
        System.out.println("CurrentPage: " + searchDto.getPage());
        System.out.println("Offset: " + searchDto.getOffset());
        
        PageResponseDto<AdminProductResponseDto> response = new PageResponseDto<>();
        List<AdminProductResponseDto> productList = adminProductMapper.findProductList(searchDto);
        System.out.println("실제 조회된 상품 개수: " + productList.size());
        // 이미지 URL에 uploads/ 접두사 추가
        productList.forEach(product -> {
            if (product.getImageUrl() != null && !product.getImageUrl().startsWith("/uploads/") && !product.getImageUrl().startsWith("uploads/")) {
                product.setImageUrl("/uploads/" + product.getImageUrl());
            }
        });
        response.setList(productList);
        response.setTotalPage(totalPage);
        response.setCurrentPage(searchDto.getPage());
        
        return response;
    }

    public AdminProductResponseDto getProduct(Integer productId) {
        AdminProductResponseDto product = adminProductMapper.findByProductId(productId);
        // 이미지 URL에 uploads/ 접두사 추가
        if (product != null && product.getImageUrl() != null && !product.getImageUrl().startsWith("/uploads/") && !product.getImageUrl().startsWith("uploads/")) {
            product.setImageUrl("/uploads/" + product.getImageUrl());
        }
        return product;
    }

    public Integer insertProduct(AdminProductRequestDto dto) {
        // 이미지 파일이 있으면 저장하고 URL 설정
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            String imageUrl = saveImageFile(dto.getImageFile());
            dto.setImageUrl(imageUrl);
        }
        
        int result = adminProductMapper.insertProduct(dto);
        
        // 상품 등록 성공 시 카테고리 매핑 저장
        if (result > 0 && dto.getCategory() != null && dto.getCategory().getCategoryId() != null) {
            categoryProductMappingMapper.insertMapping(dto.getProductId(), dto.getCategory().getCategoryId());
        }
        
        return result > 0 ? 1 : 0;
    }

    public Integer updateProduct(AdminProductRequestDto dto) {
        // 이미지 파일이 있으면 저장하고 URL 설정
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            String imageUrl = saveImageFile(dto.getImageFile());
            dto.setImageUrl(imageUrl);
        }
        
        int result = adminProductMapper.updateProduct(dto);
        
        // 상품 수정 성공 시 기존 카테고리 매핑 삭제 후 새로 저장
        if (result > 0 && dto.getCategory() != null && dto.getCategory().getCategoryId() != null) {
            categoryProductMappingMapper.deleteByProductId(dto.getProductId());
            categoryProductMappingMapper.insertMapping(dto.getProductId(), dto.getCategory().getCategoryId());
        }
        
        return result > 0 ? 1 : 0;
    }
    
    /**
     * 이미지 파일을 저장하고 URL 반환
     * 상품 이미지는 uploads/product 하위에 저장
     */
    private String saveImageFile(MultipartFile file) {
        try {
            // 상품 이미지는 uploads/product 하위에 저장
            Path uploadPath = Paths.get(uploadDir, "product");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 고유한 파일명 생성
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : "";
            String filename = UUID.randomUUID().toString() + extension;
            
            // 파일 저장
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);
            
            // URL 반환 (프론트엔드에서 접근 가능한 경로)
            return "/uploads/product/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("이미지 파일 저장 실패: " + e.getMessage(), e);
        }
    }

    public Integer deleteProduct(Integer productId) {
        // 먼저 카테고리 매핑 삭제
        categoryProductMappingMapper.deleteByProductId(productId);
        
        // 그 다음 상품 삭제
        int result = adminProductMapper.deleteProduct(productId);
        return result > 0 ? 1 : 0;
    }
}


