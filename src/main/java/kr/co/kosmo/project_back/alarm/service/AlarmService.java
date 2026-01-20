package kr.co.kosmo.project_back.alarm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.alarm.dto.AlarmDto;
import kr.co.kosmo.project_back.alarm.dto.AlarmSettingDto;
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
    public void updateSettings(AlarmSettingDto dto) {
        AlarmSettingDto current = alarmMapper.findSettingsByUserId(dto.getUserId());
        
        AlarmSettingDto next = new AlarmSettingDto();
        next.setUserId(dto.getUserId());
        // 전달 안 된 값은 기존 값 유지
        next.setIsOrderEnabled(
            dto.getIsOrderEnabled() != null
                ? dto.getIsOrderEnabled()
                : current.getIsOrderEnabled());
        next.setIsDeliveryEnabled(
            dto.getIsDeliveryEnabled() != null
                ? dto.getIsDeliveryEnabled()
                : current.getIsDeliveryEnabled());
        next.setIsAnswerEnabled(
            dto.getIsAnswerEnabled() != null
                ? dto.getIsAnswerEnabled()
                : current.getIsAnswerEnabled());
        alarmMapper.updateSettings(next);
    }
    // 문의 답변 등록 알림
    public void insertAnswerAlarm(Integer userId) {
        AlarmSettingDto setting = alarmMapper.findSettingsByUserId(userId);
        // off면 생성 안 함
        if(setting != null && Boolean.FALSE.equals(setting.getIsAnswerEnabled())) {
            return;
        }
        AlarmDto alarm = new AlarmDto();
        alarm.setUserId(userId);
        alarm.setType("ANSWER");
        alarm.setMessage("문의에 답변이 등록되었습니다.");
        alarmMapper.insertAlarm(alarm);
    }
    // 주문 상태 변경 알림
    public void insertOrderAlarm(Integer userId, String orderId) {
        AlarmSettingDto setting = alarmMapper.findSettingsByUserId(userId);
        if(setting != null && Boolean.FALSE.equals(setting.getIsOrderEnabled())) {
            return;
        }
        AlarmDto alarm = new AlarmDto();
        alarm.setUserId(userId);
        alarm.setType("ORDER");
        // 메시지에 주문 번호 등을 포함
        alarm.setMessage("주문(" + orderId + ")이 정상적으로 완료되었습니다."); 
        alarmMapper.insertAlarm(alarm);
    }
    
    // 배송 상태 변경 알림
    public void insertDeliveryStatusAlarm(Integer userId) {
        AlarmSettingDto setting = alarmMapper.findSettingsByUserId(userId);
        // off면 생성 안 함
        if(setting != null && Boolean.FALSE.equals(setting.getIsDeliveryEnabled())) {
            return;
        }
        AlarmDto alarm = new AlarmDto();
        alarm.setUserId(userId);
        alarm.setType("DELIVERY");
        alarm.setMessage("배송 상태가 변경되었습니다.");
        alarmMapper.insertAlarm(alarm);
    }
    // 알림 읽음 처리
    public void markAsRead(Integer alarmId) {
        alarmMapper.updateReadStatus(alarmId);
    }
    // 전체 읽음 처리
    public void markAllAsRead(Integer userId) {
        alarmMapper.updateAllReadStatus(userId);
    }

    // 관리자 직접 알림 추가
    // public void insertAdminAlarm(Integer userId, String message) {
    //     AlarmDto alarm = new AlarmDto();
    //     alarm.setUserId(userId);
    //     alarm.setType("ADMIN");
    //     alarm.setMessage(message);
    //     alarmMapper.insertAlarm(alarm);
    // }
}