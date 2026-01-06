package kr.co.kosmo.project_back.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import kr.co.kosmo.project_back.user.vo.CategoryVO;

@Mapper
public interface CategoryMapper {
    // 전체 관심분야 조회
    List<CategoryVO> findCategories();
}
