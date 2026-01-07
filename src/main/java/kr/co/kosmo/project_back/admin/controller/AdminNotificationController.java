package kr.co.kosmo.project_back.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/notifications")
@RequiredArgsConstructor
public class AdminNotificationController {

    @PostMapping("/send")
    public ResponseEntity<Integer> sendNotification(@RequestBody Map<String, Object> request) {
        // TODO: 알림 발송 로직 구현 필요
        return ResponseEntity.ok(1);
    }
}







