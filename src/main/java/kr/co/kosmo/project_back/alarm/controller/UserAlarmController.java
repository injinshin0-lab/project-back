package kr.co.kosmo.project_back.alarm.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // 알림 조회
    @GetMapping("/notifications/user/{userId}")
    public List<AlarmDto> getAlarmList(
        @PathVariable Integer userId,
        HttpSession session) {
        Integer loginUserId = (Integer) session.getAttribute("LOGIN_USER");
        if(loginUserId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        if(!loginUserId.equals(userId)) {
            throw new IllegalStateException("본인 알림만 조회할 수 있습니다.");
        }
        return alarmService.getAlarmList(userId);
    }
    // 알림 삭제
    @DeleteMapping("/notifications/{notificationsId}")
    public ResponseEntity<Void> deleteAlarm(
        HttpSession session, @PathVariable("notificationsId") Integer notificationId) {
            Integer loginUserId = (Integer) session.getAttribute("LOGIN_USER");
            if(loginUserId == null) {
                throw new IllegalStateException("로그인이 필요합니다.");
            }
            alarmService.deleteAlarm(notificationId);
            return ResponseEntity.noContent().build();
        }
    // 알림 설정(on/off)
    @PutMapping("/settings/{userId}/notifications")
    public ResponseEntity<Void> updateSettings(
        @PathVariable Integer userId, HttpSession session) {
            Integer loginUserId = (Integer) session.getAttribute("LOGIN_USER");
            if(loginUserId == null) {
                throw new IllegalStateException("로그인이 필요합니다.");
            }
            if(!loginUserId.equals(userId)) {
                throw new IllegalStateException("본인 설정만 변경할 수 있습니다.");
            }
            Boolean enabled = (Boolean) session.getAttribute("ALARM_ENABLED");
            Boolean next = (enabled == null) ? false : !enabled;
            session.setAttribute("ALARM_ENABLED", next);
            return ResponseEntity.ok().build();
        }
    }
