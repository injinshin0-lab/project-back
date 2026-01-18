package kr.co.kosmo.project_back.alarm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.alarm.dto.AlarmDto;
import kr.co.kosmo.project_back.alarm.mapper.AlarmMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmMapper alarmMapper;

    // 알림 조회
    public List<AlarmDto> getAlarmList(Integer userId) {
        return alarmMapper.findAlarmsByUserId(userId);
    }
    // 알림 삭제
    public void deleteAlarm(Integer alarmId) {
        alarmMapper.deleteAlarm(alarmId);
    }
    // 알림 설정
    public void insertAnswerAlarm(Integer userId, HttpSession session) {
        Boolean enabled = (Boolean) session.getAttribute("ALARM_ENABLED");

        if(enabled != null && !enabled) {
            return;     // off면 알림 x
        }
        AlarmDto alarm = new AlarmDto();
        alarm.setUserId(userId);
        alarm.setType("ANSWER");
        alarm.setMessage("문의에 답변이 등록되었습니다.");
        alarmMapper.insertAlarm(alarm);
    }

}
