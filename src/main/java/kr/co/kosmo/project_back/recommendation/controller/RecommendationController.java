package kr.co.kosmo.project_back.recommendation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.recommendation.dto.RecommendationResponseDto;
import kr.co.kosmo.project_back.recommendation.service.RecommendationService;
import kr.co.kosmo.project_back.user.dto.UserDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;

    // 인기 상품 추천
    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> getPopularRecommendations() {
        List<RecommendationResponseDto> recommendations = 
            recommendationService.getPopularRecommendations();
        Map<String, Object> response = new HashMap<>();
        response.put("recommendations", recommendations);
        response.put("type", "popular");

        return ResponseEntity.ok(response);
    }
    // 개인 맞춤 추천

    @GetMapping("/personal")
    public ResponseEntity<Map<String, Object>> getPersonalRecommendations(HttpSession session) {
        // 1. 세션에서 로그인 유저 가져오기
        Integer userId = (Integer) session.getAttribute("LOGIN_USER");
        
        List<RecommendationResponseDto> recommendations = recommendationService.getPersonalRecommendations(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("recommendations", recommendations);
        response.put("type", "personal");

        return ResponseEntity.ok(response);
    }   
}
