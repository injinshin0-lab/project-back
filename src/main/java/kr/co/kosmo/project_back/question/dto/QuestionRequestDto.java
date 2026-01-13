package kr.co.kosmo.project_back.question.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionRequestDto {
    private Integer questionId;
    private Integer userId;
    private Integer productId;
    private String title;
    private String content;
    private String type;
    private List<MultipartFile> images;
}
