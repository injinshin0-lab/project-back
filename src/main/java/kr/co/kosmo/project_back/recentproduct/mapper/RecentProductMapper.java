package kr.co.kosmo.project_back.recentproduct.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.kosmo.project_back.product.dto.ProductDto;

@Mapper
public interface RecentProductMapper {
    // 최근 본 상품 조회
    List<ProductDto> findRecentProductsByUserId(@Param("userId") Integer userId);
    // 최근 본 상품 존재 여부 확인
    int existsRecentProduct(
        @Param("userId") Integer userId,
        @Param("productId") Integer productId
    );
    int countRecentProducts(@Param("userId") Integer userId);
    // 최근 본 상품 추가
    int insertRecentProduct(
            @Param("userId") Integer userId,
            @Param("productId") Integer productId);
    // 최근 본 상품 업뎃
    int updateRecentProduct(
            @Param("userId") Integer userId,
            @Param("productId") Integer productId);
    int deleteOldestRecentProduct(@Param("userId") Integer userId);

    // 회원 탈퇴
    void deleteByUserId(@Param("userId") Integer userId);
}
