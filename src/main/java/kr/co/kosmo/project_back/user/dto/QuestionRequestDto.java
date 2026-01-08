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
public class QuestionRequestDto {
    private Integer questionId; // insert 후 생성된 ID
    private Integer userId;
    private Integer productId;
    private String title;
    private String content;
    private String type;
    private List<MultipartFile> images;
}

