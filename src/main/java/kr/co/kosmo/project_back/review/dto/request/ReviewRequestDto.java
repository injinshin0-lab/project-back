package kr.co.kosmo.project_back.review.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
    private String content;
    private Integer rating;
    private List<String> images;
}
