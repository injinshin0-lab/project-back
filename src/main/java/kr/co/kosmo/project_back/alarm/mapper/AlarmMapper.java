package kr.co.kosmo.project_back.alarm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.kosmo.project_back.alarm.dto.AlarmDto;

@Mapper
public interface AlarmMapper {
    // 알림 조회
    List<AlarmDto> findAlarmsByUserId(Integer userId);
    // 알림 삭제
    void deleteAlarm(Integer alarmId);
    // 알림 설정
    void insertAlarm(AlarmDto dto);
}
