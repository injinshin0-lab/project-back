package kr.co.kosmo.project_back.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSearchDto {
    private String startDate;
    private String endDate;
    private String questionStatus;
    private String type;
    private String keywordType;
    private String keyword;
    private Integer size = 10;
    private Integer page = 1;
    private Integer offset;

    public Integer getOffset() {
        return (page - 1) * size;
    }
}
