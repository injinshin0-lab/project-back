package kr.co.kosmo.project_back.admin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // 🔥 필수 임포트
import kr.co.kosmo.project_back.admin.dto.AlarmDto;

@Mapper
public interface AdminAlarmMapper {

    // 1. 유저별 조회
    List<AlarmDto> findAlarmsByUserId(Integer userId);

    // 2. 알림 저장
    Integer insertAlarm(AlarmDto alarm);

    // 3. 전체 조회 (이 메서드가 없어서 에러가 났던 것)
    List<AlarmDto> findAllAlarms();

    // 4. 페이지네이션 조회 (인자 2개를 명시적으로 선언)
    List<AlarmDto> findAllAlarmsPaged(@Param("limit") int limit, @Param("offset") int offset);

    // 5. 전체 개수 조회
    int countAllAlarms();
}