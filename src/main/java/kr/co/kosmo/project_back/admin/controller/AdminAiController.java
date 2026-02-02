package kr.co.kosmo.project_back.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import kr.co.kosmo.project_back.admin.dto.AiResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/ai")
@RequiredArgsConstructor
public class AdminAiController {

    private final RestTemplate restTemplate; // WebConfig의 무제한 Bean 주입

    @PostMapping({"/run", "/run/"})
    public ResponseEntity<?> runAiEngine() {
        String djangoUrl = "http://django-svc.bogam:8000/api_service/run-ai/";
        try {
            // 이제 이 요청은 장고가 응답할 때까지 끝까지 기다립니다.
            AiResponseDto response = restTemplate.postForObject(djangoUrl, new HashMap<>(), AiResponseDto.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("에러 발생: " + e.toString());
        }
    }
}
