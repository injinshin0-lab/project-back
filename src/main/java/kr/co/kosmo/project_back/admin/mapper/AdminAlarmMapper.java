package kr.co.kosmo.project_back.admin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import kr.co.kosmo.project_back.admin.dto.AlarmDto;

@Mapper
public interface AdminAlarmMapper {
    Integer insertAlarm(AlarmDto dto);
    List<AlarmDto> findAlarmsByUserId(Integer userId);
}
