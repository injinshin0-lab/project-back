package kr.co.kosmo.project_back.alarm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmSettingDto {
    private Integer userId;
    private Boolean isDeliveryEnabled;
    private Boolean enabled;
    private Boolean isAnswerEnabled;
}
