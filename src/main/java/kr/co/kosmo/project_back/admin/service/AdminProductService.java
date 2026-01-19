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
        // 카테고리 ID가 있으면 하위 카테고리 포함하여 검
    if (searchDto.getCategoryIds() != null && !searchDto.getCategoryIds().isEmpty()) {
            List<Integer> allTargetIds = new ArrayList<>(searchDto.getCategoryIds());

            for (Integer catId : searchDto.getCategoryIds()) {
                List<Integer> descendants = adminCategoryMapper.findDescendantCategoryIds(catId);
                if (descendants != null && !descendants.isEmpty()) {
                    for (Integer childId : descendants) {
                        if (!allTargetIds.contains(childId)) {
                            allTargetIds.add(childId);
                        }
                    }
                }
            }

            searchDto.setCategoryIds(allTargetIds);
        }

        else if (searchDto.getCategoryId() != null) {
            List<Integer> ids = new ArrayList<>();
            ids.add(searchDto.getCategoryId()); // 자기 자신도 포함 
            List<Integer> descendants = adminCategoryMapper.findDescendantCategoryIds(searchDto.getCategoryId());
            if (descendants != null) ids.addAll(descendants);
            searchDto.setCategoryIds(ids);
            
        }

        searchDto.setOffset((searchDto.getPage() - 1) * searchDto.getSize());

        Integer totalCount = adminProductMapper.countProductList(searchDto);
        Integer totalPage = totalCount > 0 ? (int) Math.ceil((double) totalCount / searchDto.getSize()) : 1;
        
        List<AdminProductResponseDto> productList = adminProductMapper.findProductList(searchDto);

        // 리팩토링 : 공통메서드로 이미지 경로 보정

        productList.forEach(this::formatProductImageUrl);

        PageResponseDto<AdminProductResponseDto> response  = new PageResponseDto<>();
        response.setList(productList);
        response.setTotalPage(totalPage);
        response.setCurrentPage(searchDto.getPage());

        return response;
    
    }

    //         // 하위 카테고리 ID 목록 조회
    //         List<Integer> descendantIds = adminCategoryMapper.findDescendantCategoryIds(searchDto.getCategoryId());
    //         if (descendantIds != null && !descendantIds.isEmpty()) {
    //             categoryIds.addAll(descendantIds);
    //         }
            
    //         searchDto.setCategoryIds(categoryIds);
    //     }

    //     // offset 계산
    //     Integer offset = (searchDto.getPage() - 1) * searchDto.getSize();
    //     searchDto.setOffset(offset);
        
    //     Integer totalCount = adminProductMapper.countProductList(searchDto);
    //     Integer totalPage = totalCount > 0 ? (int) Math.ceil((double) totalCount / searchDto.getSize()) : 1;
        
    //     // 디버깅: 총 개수와 페이지 정보 출력
    //     System.out.println("=== 검색 결과 디버깅 ===");
    //     System.out.println("Keyword: " + searchDto.getKeyword());
    //     System.out.println("CategoryId: " + searchDto.getCategoryId());
    //     System.out.println("CategoryIds: " + searchDto.getCategoryIds());
    //     System.out.println("TotalCount: " + totalCount);
    //     System.out.println("Size: " + searchDto.getSize());
    //     System.out.println("TotalPage: " + totalPage);
    //     System.out.println("CurrentPage: " + searchDto.getPage());
    //     System.out.println("Offset: " + searchDto.getOffset());
        
    //     PageResponseDto<AdminProductResponseDto> response = new PageResponseDto<>();
    //     List<AdminProductResponseDto> productList = adminProductMapper.findProductList(searchDto);
        
    //     productList.forEach(product -> {
    //         String url = product.getImageUrl();
    //         if (url != null && !url.isEmpty()) {
    //             // 1. 만약 /uploads/ 로 시작하지 않는 데이터라면 (기존 데이터)
    //             if (!url.startsWith("/uploads/") && !url.startsWith("http")) {
                    
    //                 // 2. 이미 product/ 로 시작하고 있다면 앞에 /uploads/만 붙임
    //                 if (url.startsWith("product/")) {
    //                     product.setImageUrl("/uploads/" + url);
    //                 } 
    //                 // 3. 그 외의 경우 (파일명만 있거나 할 때)
    //                 else {
    //                     product.setImageUrl("/uploads/product/" + url);
    //                 }
    //             }
    //         }
    //     });
    //     response.setList(productList);
    //     response.setTotalPage(totalPage);
    //     response.setCurrentPage(searchDto.getPage());
        
    //     return response;
    // }


    public AdminProductResponseDto getProduct(Integer productId) {
        AdminProductResponseDto product = adminProductMapper.findByProductId(productId);
        // 이미지 URL에 uploads/ 접두사 추가
        if (product != null) formatProductImageUrl(product);
        return product;
    }

    private void formatProductImageUrl(AdminProductResponseDto product) {
        String url = product.getImageUrl();
        if (url == null || url.isEmpty() || url.startsWith("http") || url.startsWith("/uploads/")) {
            return;
        }
        
        // 기존 데이터(product/...)와 신규 데이터 대응
        if (url.startsWith("product/")) {
            product.setImageUrl("/uploads/" + url);
        } else {
            product.setImageUrl("/uploads/product/" + url);
        }
    }

    public Integer insertProduct(AdminProductRequestDto dto) {
        // 1. 이미지 처리
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            String imageUrl = saveImageFile(dto.getImageFile());
            dto.setImageUrl(imageUrl);
        }
        
        // 2. 상품 본체 저장
        int result = adminProductMapper.insertProduct(dto);
        
        // 3. [핵심 수정] 다중 카테고리 매핑 저장
        // 기존의 dto.getCategory() != null 체크를 삭제했습니다.
        if (result > 0 && dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            categoryProductMappingMapper.insertMappings(dto.getProductId(), dto.getCategoryIds());
        }
        
        return result > 0 ? 1 : 0;
    }

    public Integer updateProduct(AdminProductRequestDto dto) {
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            String imageUrl = saveImageFile(dto.getImageFile());
            dto.setImageUrl(imageUrl);
        }
        
        int result = adminProductMapper.updateProduct(dto);
        
        // ✅ 수정된 로직: 기존 매핑 삭제 후 새로운 리스트 저장
        if (result > 0) {
            // 기존 매핑은 무조건 일단 지우고
            categoryProductMappingMapper.deleteByProductId(dto.getProductId());
            
            // 새로운 카테고리 리스트가 있으면 저장
            if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
                categoryProductMappingMapper.insertMappings(dto.getProductId(), dto.getCategoryIds());
            }
        }
        
        return result;
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


