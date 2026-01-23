package kr.co.kosmo.project_back.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.product.dto.PageResponseDto;
import kr.co.kosmo.project_back.product.dto.ProductDto;
import kr.co.kosmo.project_back.product.dto.ProductSearchDto;
import kr.co.kosmo.project_back.product.mapper.ProductMapper;
import kr.co.kosmo.project_back.product.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;
    private final SearchMapper searchMapper;

    // 상품상세 조회
    public ProductDto getProduct(Integer productId) {
        productMapper.updateViewCount(productId);
        ProductDto product = productMapper.findByProductId(productId);
        if (product != null) formatProductImageUrl(product); // ✅ 경로 보정 추가
        return product;
    }
    // 상품 검색 + 목록
    public PageResponseDto<ProductDto> searchProducts(ProductSearchDto searchDto) {
        // 전체개수

        int totalCount = productMapper.countProductList(searchDto);
        // 현재 페이지 목록

        List<ProductDto> list = productMapper.findProductList(searchDto);
        // ✅ 목록 전체 경로 보정 추가
        list.forEach(this::formatProductImageUrl);

        // 페이지 응답으로 묶음
        return PageResponseDto.of(
            list,
            totalCount,
            searchDto.getPage(),
            searchDto.getSize()
        );
    }

    // ✅ 이미지 경로 보정 함수 (AdminProductService와 동일한 로직 유지)
    private void formatProductImageUrl(ProductDto product) {
        String url = product.getImageUrl();

        if (url == null || url.isEmpty() || url.startsWith("http") || url.startsWith("/uploads/")) {
            return;
        }
        
        String resultUrl;

        // 기존 데이터 및 신규 날짜형 데이터 모두 대응
        if (url.startsWith("product/")) {
            resultUrl = "/uploads/" + url;
        } else {
            resultUrl = "/uploads/product/" + url;
        }
        product.setImageUrl(resultUrl.replace(" ", "%20"));
    }


    // 인기 검색어 
    public List<String> getPopularKeywords() {
        return searchMapper.findPopularKeywords();
    }

    // 인기 상품
    public List<ProductDto> getPopularProducts() {
        List<ProductDto> list = productMapper.findTopSalesProducts();
        // ✅ 이 줄을 추가해서 인기 상품 목록도 경로를 보정해줘야 합니다!
        list.forEach(this::formatProductImageUrl);
        return list;
    }

    // 조회순 상품 가져오기
    public List<ProductDto> getTopViewProducts() {
        List<ProductDto> list = productMapper.findTopViewProducts();
        list.forEach(this::formatProductImageUrl);
        return list;
    }
}

    