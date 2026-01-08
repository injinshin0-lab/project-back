package kr.co.kosmo.project_back.user.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDto {
    private Integer inquiryAlarmId; // 문의 알림 ID (type이 '문의완료'인 경우)
    private Integer deliveryAlarmId; // 배송 알림 ID (type이 '대기중', '배송중', '배송완료'인 경우)
    private Integer userId;
    private String title;
    private String type; // '문의완료', '대기중', '배송중', '배송완료'
    private String content;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}

