package kr.co.kosmo.project_back.wishlist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.product.dto.ProductDto;
import kr.co.kosmo.project_back.wishlist.mapper.WishlistMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistMapper wishlistMapper;

    // 찜 목록 조회
    public List<ProductDto> getWishList(Integer userId) {
        return wishlistMapper.findWishListByUserId(userId);
    }
    // 찜 목록 추가/삭제
    public List<ProductDto> toggleWish(Integer userId, Integer productId) {
        // 찜 유무 확인
        int count = wishlistMapper.findWish(userId, productId);
        // 있으면 삭제/없으면 추가
        if(count > 0) {
            wishlistMapper.deleteWish(userId, productId);
        } else {
            wishlistMapper.insertWish(userId, productId);
        }
        // 변경 찜 목록 조회 후 반환
        return wishlistMapper.findWishListByUserId(userId);
    }



    
}
