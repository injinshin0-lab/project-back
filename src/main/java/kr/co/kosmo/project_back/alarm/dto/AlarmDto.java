package kr.co.kosmo.project_back.alarm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmDto {
    private Integer id;
    private Integer userId;
    private String type;
    private String message;
}
