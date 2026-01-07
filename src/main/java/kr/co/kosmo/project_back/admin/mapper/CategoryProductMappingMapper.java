package kr.co.kosmo.project_back.admin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryProductMappingMapper {
    /**
     * 상품의 카테고리 매핑 삭제
     */
    Integer deleteByProductId(Integer productId);
    
    /**
     * 상품의 카테고리 매핑 추가
     */
    Integer insertMapping(Integer productId, Integer interestCategoryId);
    
    /**
     * 상품의 카테고리 ID 목록 조회
     */
    List<Integer> findCategoryIdsByProductId(Integer productId);
}




