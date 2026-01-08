package kr.co.kosmo.project_back.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import kr.co.kosmo.project_back.user.dto.AlarmDto;

@Mapper
public interface AlarmMapper {
    Integer insertAlarm(AlarmDto dto);
    List<AlarmDto> findAlarmsByUserId(Integer userId);
}

