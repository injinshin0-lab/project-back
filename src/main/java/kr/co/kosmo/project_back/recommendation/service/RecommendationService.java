package kr.co.kosmo.project_back.recommendation.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.recommendation.dto.RecommendationResponseDto;
import kr.co.kosmo.project_back.recommendation.mapper.RecommendationMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationMapper recommendationMapper;

    // 추천 상품
    public List<RecommendationResponseDto> getPopularRecommendations() {
        return recommendationMapper.findTop10ByUserId(null);
    }
    // 개인별 맞춤 추천
    public List<RecommendationResponseDto> getPersonalRecommendations(Integer userId) {
         // 먼저 회원가입 시 선택한 관심분야들을 추천 후보로 가져온다
        List<RecommendationResponseDto> candidates = recommendationMapper.findByUserInterest(userId);
    // 상품별 점수판 생성
    Map<Integer, Integer> scoreMap = new HashMap<>();
    for(RecommendationResponseDto dto : candidates) {
        scoreMap.put(dto.getProductId(), 0);
    }
    // 구매 이력 반영
    for(Integer productId : recommendationMapper.findPurchasedProductIds(userId)) {
        // 후보 상품에 포함된 경우만 점수 반영
        if(scoreMap.containsKey(productId)) {
            int currentScore = scoreMap.get(productId);
            scoreMap.put(productId, currentScore + 5);
        }
    }
    // 찜 반영
    for(Integer productId : recommendationMapper.findWishProductIds(userId)) {
        // 후보 상품에 포함된 경우만 점수 반영
        if(scoreMap.containsKey(productId)) {
            int currentScore = scoreMap.get(productId);
            scoreMap.put(productId, currentScore + 3);
        }
    }
    // 장바구니 반영
    for(Integer productId : recommendationMapper.findCartProductIds(userId)) {
        // 후보 상품에 포함된 경우만 점수 반영
        if(scoreMap.containsKey(productId)) {
            int currentScore = scoreMap.get(productId);
            scoreMap.put(productId, currentScore + 2);
        }
    }
    // 최근 본 상품 반영
    for(Integer productId : recommendationMapper.findRecentViewedProductIds(userId)) {
        // 후보 상품에 포함된 경우만 점수 반영
        if(scoreMap.containsKey(productId)) {
            int currentScore = scoreMap.get(productId);
            scoreMap.put(productId, currentScore + 1);
        }
    }
    // 계산된 점수 복사(Dto에 담기 위해)
    for(RecommendationResponseDto dto : candidates) {
        dto.setRank(scoreMap.get(dto.getProductId()));
    }
    // 기존 추천 삭제(항상 최신 추천 유지)
        recommendationMapper.deleteAiRecommendationByUser(userId);
    // 점수 있는 것만 저장
    for(RecommendationResponseDto dto : candidates) {
        if(dto.getRank() > 0) {
            recommendationMapper.insertAiRecommendation(
                userId, dto.getProductId(), dto.getRank());
        }
    }
    // 최종 개인 맞춤 추천 리스트 반환
    return recommendationMapper.findAiRecommendationByUser(userId);
    }
}
