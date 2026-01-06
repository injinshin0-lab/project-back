package kr.co.kosmo.project_back.review.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
    private Long reviewId;
    private Integer userId;
    private Integer productId;
    private String content;
    private Integer rating;
    private List<MultipartFile> images;
}
