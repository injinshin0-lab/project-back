package kr.co.kosmo.project_back.admin.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminReviewResponseDto {
    private Integer reviewId;
    private Integer userId;
    private String loginId;
    private String content;
    private Double rating;
    private List<AdminReviewImageDto> reviewImages;
}


