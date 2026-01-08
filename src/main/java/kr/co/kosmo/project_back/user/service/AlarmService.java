package kr.co.kosmo.project_back.user.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.user.dto.AlarmDto;
import kr.co.kosmo.project_back.user.mapper.AlarmMapper;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmMapper alarmMapper;

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
}

