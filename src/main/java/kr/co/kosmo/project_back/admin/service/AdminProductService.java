package kr.co.kosmo.project_back.admin.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class AdminProductService {
    private final AdminProductMapper adminProductMapper;
    private final AdminCategoryMapper adminCategoryMapper;
    private final CategoryProductMappingMapper categoryProductMappingMapper;
    
    @Value("${file.upload.review-path}")
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
            String original = file.getOriginalFilename();
            String ext = "";
            if(original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."));
            }

            String fileName = UUID.randomUUID() + ext;

            // 프로젝트 루트 기준 절대경로 만들기 (ReviewService 방식)
            String datePath = LocalDate.now().getYear() + "/" + String.format("%02d", LocalDate.now().getMonthValue());
            // uploadDir는 C:/ai/Workspace/bogam/storage 로 설정되어 있어야 함
            String dirPath = uploadDir + "/product/" + datePath; 
            
            File dir = new File(dirPath);
            if(!dir.exists()) dir.mkdirs();

            // 실제 저장 파일 경로
            File saveFile = new File(dir, fileName);
            file.transferTo(saveFile);

            // DB 저장 URL (프론트엔드 노출 경로)
            return "/uploads/product/" + datePath + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("상품 이미지 저장 실패", e);
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


