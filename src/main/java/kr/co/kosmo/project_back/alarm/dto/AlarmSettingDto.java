package kr.co.kosmo.project_back.alarm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmSettingDto {
    private Integer userId;
    private Boolean isDeliveryEnabled;      // 배송상태변경알림
    private Boolean isOrderEnabled;         // 주문상태변경알림
    private Boolean isAnswerEnabled;        // 문의답변등록알림
}
