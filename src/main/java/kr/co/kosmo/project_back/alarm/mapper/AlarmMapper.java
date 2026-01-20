package kr.co.kosmo.project_back.alarm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.kosmo.project_back.alarm.dto.AlarmDto;
import kr.co.kosmo.project_back.alarm.dto.AlarmSettingDto;

@Mapper
public interface AlarmMapper {
    // 알림 조회
    List<AlarmDto> findAlarmsByUserId(Integer userId);
    // 알림 삭제
    void deleteAlarm(Integer alarmId);
    // 알림 생성
    void insertAlarm(AlarmDto dto);
    // 알림 설정
    AlarmSettingDto findSettingsByUserId(Integer userId);
    void updateSettings(AlarmSettingDto dto);
    // 알림 읽음 처리
    void updateReadStatus(Integer alarmId);
    // 알림 전체 읽음 처리
    void updateAllReadStatus(Integer userId);

    // 주문 상태 업데이트 추가
    void updateOrderStatus(@Param("orderId") Integer orderId, @Param("status") String status);
}
