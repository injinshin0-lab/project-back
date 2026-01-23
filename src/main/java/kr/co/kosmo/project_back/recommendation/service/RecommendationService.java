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

    // 개인별 맞춤 추천: 장고가 계산해둔 결과물만 그대로 조회
    public List<RecommendationResponseDto> getPersonalRecommendations(Integer userId) {
        List<RecommendationResponseDto> recommendations = recommendationMapper.findAiRecommendationByUser(userId);
        
        // ✅ 이미지 경로 보정 로직 추가
        recommendations.forEach(this::formatRecommendationImageUrl);
        
        return recommendations;
    }

    public List<RecommendationResponseDto> getPopularRecommendations() {
        List<RecommendationResponseDto> list = recommendationMapper.findTop10ByUserId(null);
        
        // ✅ 여기도 추가
        list.forEach(this::formatRecommendationImageUrl);
        
        return list;
    }

    private void formatRecommendationImageUrl(RecommendationResponseDto dto) {
        String url = dto.getImageUrl();

        if (url == null || url.isEmpty() || url.startsWith("http") || url.startsWith("/uploads/")) {
            return;
        }
        
        String resultUrl;
        if (url.startsWith("product/")) {
            resultUrl = "/uploads/" + url;
        } else {
            resultUrl = "/uploads/product/" + url;
        }
        dto.setImageUrl(resultUrl.replace(" ", "%20"));
    }
}