package kr.co.kosmo.project_back.user.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.user.dto.ProductDto;
import kr.co.kosmo.project_back.user.mapper.WishlistMapper;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistMapper wishlistMapper;

    public Integer toggleWish(Integer userId, Integer productId) {
        Integer exists = wishlistMapper.existsWish(userId, productId);
        if (exists != null && exists > 0) {
            // 이미 있으면 삭제
            return wishlistMapper.deleteWish(userId, productId);
        } else {
            // 없으면 추가
            return wishlistMapper.insertWish(userId, productId);
        }
    }

    public List<ProductDto> getWishList(Integer userId) {
        List<ProductDto> wishList = wishlistMapper.findWishListByUserId(userId);
        // 이미지 URL에 uploads/ 접두사 추가
        wishList.forEach(product -> {
            if (product.getImageUrl() != null && !product.getImageUrl().startsWith("/uploads/") && !product.getImageUrl().startsWith("uploads/")) {
                product.setImageUrl("/uploads/" + product.getImageUrl());
            }
        });
        return wishList;
    }
}

