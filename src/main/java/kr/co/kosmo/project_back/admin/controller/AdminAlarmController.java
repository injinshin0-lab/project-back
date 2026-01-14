package kr.co.kosmo.project_back.admin.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.AlarmDto;
import kr.co.kosmo.project_back.admin.service.AdminAlarmService;

@RestController
@RequestMapping("/api/v1/alarm")
@RequiredArgsConstructor
public class AdminAlarmController {
    private final AdminAlarmService alarmService;

    @GetMapping
    public ResponseEntity<List<AlarmDto>> getAlarmList(@RequestParam Integer userId) {
        return ResponseEntity.ok(alarmService.getAlarmList(userId));
    }

    @PutMapping("/read/{notificationsId}")
    public ResponseEntity<Integer> markNotificationAsRead(@PathVariable Integer notificationsId) {
        // TODO: 알림 읽음 처리 로직 구현 필요
        return ResponseEntity.ok(1);
    }

    @PutMapping("/read-all")
    public ResponseEntity<Integer> markAllNotificationsAsRead(@RequestParam Integer userId) {
        // TODO: 알림 전체 읽음 처리 로직 구현 필요
        return ResponseEntity.ok(1);
    }

    @DeleteMapping("/{notificationsId}")
    public ResponseEntity<Integer> deleteNotification(@PathVariable Integer notificationsId) {
        // TODO: 알림 삭제 로직 구현 필요
        return ResponseEntity.ok(1);
    }
}
