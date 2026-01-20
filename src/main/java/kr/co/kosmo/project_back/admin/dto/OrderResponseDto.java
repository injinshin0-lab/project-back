package kr.co.kosmo.project_back.admin.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Integer orderId;
    private Integer userId;
    private String loginId;
    private String recipient;
    private String recipientPhone;
    private String payType;
    private String postcode;
    private String address;
    private String detailAddress;
    private Integer totalPrice;
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    private List<CartDto> orderItems;
}
