package kr.co.kosmo.project_back.user.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.CategoryDto;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.admin.dto.ProductSearchDto;
import kr.co.kosmo.project_back.admin.mapper.AdminCategoryMapper;
import kr.co.kosmo.project_back.user.dto.ProductDto;
import kr.co.kosmo.project_back.user.mapper.ProductMapper;
import kr.co.kosmo.project_back.user.mapper.RecentProductMapper;
import kr.co.kosmo.project_back.user.mapper.SearchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductMapper productMapper;
    private final RecentProductMapper recentProductMapper;
    private final SearchMapper searchMapper;
    private final AdminCategoryMapper adminCategoryMapper;

    public PageResponseDto<ProductDto> getProductList(ProductSearchDto searchDto) {
        // 검색 키워드가 있으면 검색 기록 저장 (테이블이 없을 수 있으므로 예외 처리)
        if (searchDto.getKeyword() != null && !searchDto.getKeyword().isEmpty() && searchDto.getUserId() != null) {
            try {
                searchMapper.insertSearchHistory(searchDto.getUserId(), searchDto.getKeyword());
            } catch (Exception e) {
                log.debug("검색 기록 저장 실패 (테이블이 없을 수 있음): {}", e.getMessage());
            }
        }

        // 카테고리 ID가 있으면 하위 카테고리 포함하여 검색
        if (searchDto.getCategoryId() != null) {
            try {
                List<Integer> categoryIds = new ArrayList<>();
                categoryIds.add(searchDto.getCategoryId()); // 자기 자신도 포함
                
                // 하위 카테고리 ID 목록 조회
                List<Integer> descendantIds = adminCategoryMapper.findDescendantCategoryIds(searchDto.getCategoryId());
                if (descendantIds != null && !descendantIds.isEmpty()) {
                    categoryIds.addAll(descendantIds);
                }
                
                searchDto.setCategoryIds(categoryIds);
            } catch (Exception e) {
                log.debug("하위 카테고리 조회 실패 (무시): {}", e.getMessage());
                // 실패 시 현재 카테고리만 사용
                List<Integer> categoryIds = new ArrayList<>();
                categoryIds.add(searchDto.getCategoryId());
                searchDto.setCategoryIds(categoryIds);
            }
        }

        // page와 size 기본값 설정
        if (searchDto.getPage() == null || searchDto.getPage() < 1) {
            searchDto.setPage(1);
        }
        if (searchDto.getSize() == null || searchDto.getSize() < 1) {
            searchDto.setSize(10);
        }
        
        // offset 계산
        Integer offset = (searchDto.getPage() - 1) * searchDto.getSize();
        searchDto.setOffset(offset);
        
        Integer totalCount = productMapper.countProductList(searchDto);
        Integer totalPage = totalCount > 0 ? (int) Math.ceil((double) totalCount / searchDto.getSize()) : 1;
        
        PageResponseDto<ProductDto> response = new PageResponseDto<>();
        List<ProductDto> productList = productMapper.findProductList(searchDto);
        
        // null 체크 및 이미지 URL에 uploads/ 접두사 추가
        if (productList != null) {
            for (ProductDto product : productList) {
                try {
                    if (product != null && product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                        String imageUrl = product.getImageUrl().trim();
                        if (!imageUrl.startsWith("/uploads/") && !imageUrl.startsWith("uploads/")) {
                            product.setImageUrl("/uploads/" + imageUrl);
                        }
                    }
                } catch (Exception e) {
                    log.warn("이미지 URL 처리 중 오류 발생 (무시): {}", e.getMessage());
                }
            }
        }
        
        response.setList(productList != null ? productList : new ArrayList<>());
        response.setTotalPage(totalPage);
        response.setCurrentPage(searchDto.getPage());
        
        return response;
    }

    public ProductDto getProduct(Integer productId, Integer userId) {
        // 최근 본 상품 추가 (잠금 오류 시 무시하고 계속 진행)
        if (userId != null) {
            try {
                recentProductMapper.insertOrUpdateRecentProduct(userId, productId);
            } catch (Exception e) {
                // 데이터베이스 잠금 오류 등 발생 시 로그만 출력하고 계속 진행
                log.debug("최근 본 상품 저장 실패 (무시): {}", e.getMessage());
            }
        }
        ProductDto product = productMapper.findByProductId(productId, userId);
        // 이미지 URL에 uploads/ 접두사 추가
        if (product != null && product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            try {
                String imageUrl = product.getImageUrl().trim();
                if (!imageUrl.startsWith("/uploads/") && !imageUrl.startsWith("uploads/")) {
                    product.setImageUrl("/uploads/" + imageUrl);
                }
            } catch (Exception e) {
                log.warn("이미지 URL 처리 중 오류 발생 (무시): {}", e.getMessage());
            }
        }
        return product;
    }

    public List<CategoryDto> getAllCategories() {
        return productMapper.findAllCategories();
    }
}

