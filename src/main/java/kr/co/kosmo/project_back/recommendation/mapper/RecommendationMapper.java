package kr.co.kosmo.project_back.recommendation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.kosmo.project_back.recommendation.dto.RecommendationResponseDto;

@Mapper
public interface RecommendationMapper {
    // 인기 상품 추천 (최근 판매량 기준)
    List<RecommendationResponseDto> findTop10ByUserId(Integer userId);
    // 개인 맞춤 추천 - 관심분야 기반 후보 
    List<RecommendationResponseDto> findByUserInterest(Integer userId);

    // 개인 맞춤 추천 행동 데이터 //
    // 구매 이력 
    List<Integer> findPurchasedProductIds(Integer userId);
    // 찜 이력
    List<Integer> findWishProductIds(Integer userId);
    // 장바구니 이력
    List<Integer> findCartProductIds(Integer userId);
    // 최근 본 상품 이력
    List<Integer> findRecentViewedProductIds(Integer userId);
    // AI 추천 결과 저장용 //
    // 기존 추천 삭제 (최신 추천 유지용)
    void deleteAiRecommendationByUser(Integer userId);
    // 추천 결과 저장
    void insertAiRecommendation(Integer userId, Integer productId, Integer score);
    // 저장된 추천 조회
    List<RecommendationResponseDto> findAiRecommendationByUser(Integer userId);

    }