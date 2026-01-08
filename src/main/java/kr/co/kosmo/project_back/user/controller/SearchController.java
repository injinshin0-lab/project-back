package kr.co.kosmo.project_back.user.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/popular-searches")
@RequiredArgsConstructor
public class SearchController {

    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getPopularSearches() {
        // TODO: 인기 검색어 조회 로직 구현 필요
        return ResponseEntity.ok(List.of());
    }
}







