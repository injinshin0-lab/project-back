package kr.co.kosmo.project_back.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.AlarmDto;
import kr.co.kosmo.project_back.admin.mapper.AdminAlarmMapper;
import kr.co.kosmo.project_back.alarm.dto.AlarmSettingDto;
import kr.co.kosmo.project_back.alarm.mapper.AlarmMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAlarmService {
    private final AdminAlarmMapper adminAlarmMapper;
    private final AlarmMapper alarmMapper;

    // ✅ 내부에서만 사용하는 상태 메시지 생성 메서드
    private String generateStatusMessage(String status) {
        if (status == null) return "주문 상태가 변경되었습니다.";
        
        // 공백을 제거하고 비교하여 오작동 방지
        switch(status.replace(" ", "")) {
            case "결제완료": 
                return "결제 완료되었습니다. 정상적으로 처리되었습니다.";
            case "상품준비중":
            case "상품준비":
                return "상품 준비중입니다. 조금만 기다려주세요.";
            case "배송중":
            case "배송 중":
                return "배송 중입니다. 상품 배송이 시작되었습니다!";
            case "배송완료":
                return "배송 완료되었습니다. 상품은 만족스러우신가요?";
            case "주문취소":
                return "주문 취소되었습니다. 이용해 주셔서 감사합니다.";
            default:
                return "주문 상태가 [" + status + "]로 변경되었습니다.";
        }
    }

    public Integer insertAnswerAlarm(Integer userId, String questionTitle) {
        
        AlarmSettingDto setting = alarmMapper.findSettingsByUserId(userId);
        
        // 문의 답변 알림 설정이 OFF라면 리턴
        if (setting != null && Boolean.FALSE.equals(setting.getIsAnswerEnabled())) {
            return 0; // 혹은 null
        }

        AlarmDto alarm = new AlarmDto();
        alarm.setUserId(userId);
        alarm.setType("문의완료");
        alarm.setContent("문의 '" + questionTitle + "'에 답변이 등록되었습니다.");
        return adminAlarmMapper.insertAlarm(alarm);
    }

    public Integer insertDeliveryAlarm(Integer userId, Integer orderId, String status) {
        AlarmSettingDto setting = alarmMapper.findSettingsByUserId(userId);
        if (setting != null && Boolean.FALSE.equals(setting.getIsDeliveryEnabled())) {
            return 0; 
        }
        
        String orderNo = "ORD-" + String.format("%03d", orderId);

        String statusMsg = generateStatusMessage(status);

        AlarmDto alarm = new AlarmDto();
        alarm.setUserId(userId);
        alarm.setType("DELIVERY");

        alarm.setContent(String.format("[%s] %s", orderNo, statusMsg));
        return adminAlarmMapper.insertAlarm(alarm);
    }

    @Transactional(readOnly = true)
    public List<AlarmDto> getAlarmList(Integer userId) {
        return adminAlarmMapper.findAlarmsByUserId(userId);
    }

    /**
     * 관리자가 특정 회원에게 알림 전송
     * @param userId 회원 ID
     * @param message 알림 메시지
     * @return 생성된 알림 ID
     */

    public Integer insertAdminAlarm(Integer userId, String message) {
    // 1. 중복 선언된 AlarmDto alarm 변수를 하나로 통일하거나 안으로 넣습니다.
    
    if (userId == null) {
            return adminAlarmMapper.insertAllUserAlarm("전체공지", message);
        } else {
            AlarmDto alarm = new AlarmDto(); 
            alarm.setUserId(userId);
            alarm.setType("관리자알림");
            alarm.setContent(message);
            return adminAlarmMapper.insertAlarm(alarm);
        }
    }


    public Map<String, Object> getAllAlarmListPaged(int page, int size) {
        int offset = (page - 1) * size;
        List<AlarmDto> list = adminAlarmMapper.findAllAlarmsPaged(size, offset);
        int totalCount = adminAlarmMapper.countAllAlarms();

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", totalCount);
        return result;
    }

    public Integer insertAdminAlarmByLoginId(String loginId, String message) {
        Integer userId = adminAlarmMapper.findIdByLoginId(loginId);

        if (userId == null) {
            throw new RuntimeException("존재하지 않는 사용자 아이디입니다.");
        }

        return insertAdminAlarm(userId, message);
    }
    
}
