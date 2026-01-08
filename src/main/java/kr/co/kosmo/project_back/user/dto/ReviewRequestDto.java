package kr.co.kosmo.project_back.user.dto;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    private Integer reviewId; // insert 후 생성된 ID
    private Integer userId;
    private Integer productId;
    private Integer orderId; // 주문 ID 추가
    private String content;
    private Double rating;
    private List<MultipartFile> images;
}

