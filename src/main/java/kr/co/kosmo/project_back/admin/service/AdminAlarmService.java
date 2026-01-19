package kr.co.kosmo.project_back.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.AlarmDto;
import kr.co.kosmo.project_back.admin.mapper.AdminAlarmMapper;

@Service
@RequiredArgsConstructor
public class AdminAlarmService {
    private final AdminAlarmMapper alarmMapper;

    public Integer insertAnswerAlarm(Integer userId, String questionTitle) {
        AlarmDto alarm = new AlarmDto();
        alarm.setUserId(userId);
        alarm.setType("문의완료");
        alarm.setContent("문의 '" + questionTitle + "'에 답변이 등록되었습니다.");
        return alarmMapper.insertAlarm(alarm);
    }

    public Integer insertDeliveryAlarm(Integer userId, Integer orderId, String status) {
        AlarmDto alarm = new AlarmDto();
        alarm.setUserId(userId);
        // 상태를 그대로 type으로 사용 (대기중, 배송중, 배송완료)
        alarm.setType(status);
        alarm.setContent("주문 #" + orderId + "의 배송 상태가 '" + status + "'로 변경되었습니다.");
        return alarmMapper.insertAlarm(alarm);
    }

    public List<AlarmDto> getAlarmList(Integer userId) {
        return alarmMapper.findAlarmsByUserId(userId);
    }

    /**
     * 관리자가 특정 회원에게 알림 전송
     * @param userId 회원 ID
     * @param message 알림 메시지
     * @return 생성된 알림 ID
     */
    public Integer insertAdminAlarm(Integer userId, String message) {
        AlarmDto alarm = new AlarmDto();
        alarm.setUserId(userId);
        alarm.setType("관리자알림");
        alarm.setContent(message);
        return alarmMapper.insertAlarm(alarm);
    }


    public Map<String, Object> getAllAlarmListPaged(int page, int size) {
        int offset = (page - 1) * size;
        List<AlarmDto> list = alarmMapper.findAllAlarmsPaged(size, offset);
        int totalCount = alarmMapper.countAllAlarms();

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", totalCount);
        return result;
    }
    
}
