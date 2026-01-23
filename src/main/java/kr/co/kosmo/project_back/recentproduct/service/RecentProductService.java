package kr.co.kosmo.project_back.recentproduct.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kosmo.project_back.product.dto.ProductDto;
import kr.co.kosmo.project_back.recentproduct.mapper.RecentProductMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecentProductService {
    private final RecentProductMapper recentProductmapper;

    // 최근 본 상품 조회
    public List<ProductDto> getRecentProductList(Integer userId) {
        return recentProductmapper.findRecentProductsByUserId(userId);
    }
    // 최근 본 상품 추가
    @Transactional
    public void insertOrUpdateRecentProduct(Integer userId, Integer productId) {
        int exists = recentProductmapper.existsRecentProduct(userId, productId);
        if(exists > 0) {        // 이미 있다면 시간 갱신
            recentProductmapper.updateRecentProduct(userId, productId);
        } else {                // 없으면 개수 확인
            int count = recentProductmapper.countRecentProducts(userId);
            if(count >= 10) {
                recentProductmapper.deleteOldestRecentProduct(userId);
            }
            // 새로 추가
            recentProductmapper.insertRecentProduct(userId, productId);
        }      
    }
}
