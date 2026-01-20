package kr.co.kosmo.project_back.alarm.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.alarm.dto.AlarmDto;
import kr.co.kosmo.project_back.alarm.dto.AlarmSettingDto;
import kr.co.kosmo.project_back.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserAlarmController {
    private final AlarmService alarmService;

    // 알림 목록 조회
    @GetMapping("/notifications/user/{userId}")
    public ResponseEntity<List<AlarmDto>> getAlarmList(
            @PathVariable Integer userId,
            HttpSession session) {
        Integer loginUserId = (Integer) session.getAttribute("LOGIN_USER");
        if(loginUserId == null) {
            return ResponseEntity.status(401).build();
        }
        if(!loginUserId.equals(userId)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(alarmService.getAlarmList(userId));
    }
    // 알림 삭제
    @DeleteMapping("/notifications/{notificationsId}")
    public ResponseEntity<Void> deleteAlarm(
            @PathVariable("notificationsId") Integer notificationId,
            HttpSession session) {
        Integer loginUserId = (Integer) session.getAttribute("LOGIN_USER");
        if(loginUserId == null) {
            return ResponseEntity.status(401).build();
        }
        alarmService.deleteAlarm(notificationId);
        return ResponseEntity.noContent().build();
    }
    // 알림 설정
    @PutMapping("/settings/{userId}/notifications")
    public ResponseEntity<Void> updateSettings(
            @PathVariable Integer userId,
            @RequestBody AlarmSettingDto dto,
            HttpSession session) {
        Integer loginUserId = (Integer) session.getAttribute("LOGIN_USER");
        if(loginUserId == null) {
            return ResponseEntity.status(401).build();
        }
        if(!loginUserId.equals(userId)) {
            return ResponseEntity.status(403).build();
        }
        dto.setUserId(userId);
        alarmService.updateSettings(dto);

        return ResponseEntity.ok().build();
    }
    // 알림 읽음 처리
    @PatchMapping("/notifications/{notificationsId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable("notificationsId") Integer alarmId) {
                alarmService.markAsRead(alarmId);
                return ResponseEntity.ok().build();
    }
    // 알림 전체 읽음 처리
    @PatchMapping("/notifications/user/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @PathVariable Integer userId) {
                alarmService.markAllAsRead(userId);
                return ResponseEntity.ok().build();
    }
}
