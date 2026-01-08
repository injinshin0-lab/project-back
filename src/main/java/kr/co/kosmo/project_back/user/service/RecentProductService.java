package kr.co.kosmo.project_back.user.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.user.dto.ProductDto;
import kr.co.kosmo.project_back.user.mapper.RecentProductMapper;

@Service
@RequiredArgsConstructor
public class RecentProductService {
    private final RecentProductMapper recentProductMapper;

    public List<ProductDto> getRecentProductList(Integer userId) {
        List<ProductDto> recentProducts = recentProductMapper.findRecentProductsByUserId(userId);
        // 이미지 URL에 uploads/ 접두사 추가
        recentProducts.forEach(product -> {
            if (product.getImageUrl() != null && !product.getImageUrl().startsWith("/uploads/") && !product.getImageUrl().startsWith("uploads/")) {
                product.setImageUrl("/uploads/" + product.getImageUrl());
            }
        });
        return recentProducts;
    }

    public Integer deleteRecentProduct(Integer userId, Integer productId) {
        return recentProductMapper.deleteRecentProduct(userId, productId);
    }
}

