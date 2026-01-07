package kr.co.kosmo.project_back.admin.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductRequestDto {
    private Integer productId;
    private String productName;
    private AdminCategoryDto category;
    private String originName;
    private String content;
    private Integer price;
    private MultipartFile imageFile;
    private String imageUrl;
}

