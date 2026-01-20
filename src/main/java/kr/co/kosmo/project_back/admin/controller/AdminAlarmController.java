package kr.co.kosmo.project_back.admin.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/alarm")
@RequiredArgsConstructor
public class AdminAlarmController {
    private final AdminAlarmService alarmService;

    @GetMapping
    public ResponseEntity<List<AlarmDto>> getAlarmList(@RequestParam Integer userId) {
        return ResponseEntity.ok(alarmService.getAlarmList(userId));
    }

    @PostMapping("/send")
    public ResponseEntity<Integer> sendAlarm(@RequestBody AlarmDto alarmDto) {
        
        // 1. 전체발송일 때 (아이디가 아예 없을때)
        if (alarmDto.getUserLoginId() == null && alarmDto.getUserId() == null) {
            return ResponseEntity.ok(alarmService.insertAdminAlarm(null, alarmDto.getContent()));
        }

        // 특정 유저 발송 (문자 아이디가 들어왔을 때) 
        if (alarmDto.getUserLoginId() != null) {
            return ResponseEntity.ok(alarmService.insertAdminAlarmByLoginId(alarmDto.getUserLoginId(), alarmDto.getContent()));
        }

        // ID 숫자가 들어왔을때
        return ResponseEntity.ok(alarmService.insertAdminAlarm(alarmDto.getUserId(), alarmDto.getContent()));
    }

    // 모든 알람찾기
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllAlarms( // 👈 리턴 타입을 Map으로 변경
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size) {
        
        // 👈 서비스의 'Paged' 메서드를 호출해야 합니다!
        return ResponseEntity.ok(alarmService.getAllAlarmListPaged(page, size));
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
