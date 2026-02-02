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

    public List<RecommendationResponseDto> getPersonalRecommendations(Integer userId) {
        // ❌ formatRecommendationImageUrl 호출을 삭제하세요.
        return recommendationMapper.findAiRecommendationByUser(userId);
    }

    public List<RecommendationResponseDto> getPopularRecommendations() {
        // ❌ formatRecommendationImageUrl 호출을 삭제하세요.
        return recommendationMapper.findTop10ByUserId(null);
    }

    // ❌ formatRecommendationImageUrl 메서드 전체를 삭제하거나 주석 처리하세요.
    // 주소 보정은 오직 WebConfig(Serializer) 한 곳에서만 처리하는 것이 가장 안전합니다.
}