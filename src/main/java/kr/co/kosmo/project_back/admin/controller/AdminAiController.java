package kr.co.kosmo.project_back.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import kr.co.kosmo.project_back.admin.dto.AiResponseDto;

@RestController
@RequestMapping("/api/v1/admin/ai")
public class AdminAiController {
    
    @PostMapping("/run")
    public ResponseEntity<?> runAiEngine(){
        // 장고 api 주소
        String djangoUrl = "http://localhost:8000/api_service/run-ai/";

        // 외부 API와 통신하기 위한 스프링 도구
        RestTemplate restTemplate = new RestTemplate();
        
        try {
            // 장고에게 POST 요청을 보내고 결과를 DTO로 받는다
            // 두번째 인자는 보낼 데이터인데 지금은 필요없으니까 null
            AiResponseDto response = restTemplate.postForObject(djangoUrl, null, AiResponseDto.class);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "AI 엔진서버 연결에 실패했습니다. : " + e.getMessage());
            return ResponseEntity.status(500).body(errorMap);
        }
    }
}
