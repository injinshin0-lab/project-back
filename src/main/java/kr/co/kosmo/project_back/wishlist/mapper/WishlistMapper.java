package kr.co.kosmo.project_back.wishlist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.kosmo.project_back.product.dto.ProductDto;

@Mapper
public interface WishlistMapper {
    // 찜 목록 조회
    List<ProductDto> findWishListByUserId(Integer userId);
    // 찜 여부 확인
    int findWish(Integer userId, Integer productId);
    // 찜 목록 추가
    void insertWish(Integer userId, Integer productId);
    // 찜 목록 삭제
    void deleteWish(Integer userId, Integer productId);
}
