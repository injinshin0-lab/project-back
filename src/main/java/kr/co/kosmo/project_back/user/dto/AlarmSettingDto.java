package kr.co.kosmo.project_back.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmSettingDto {
    private Integer userId;
    private Boolean isDeliveryEnabled;
    private Boolean isAnswerEnabled;
}








