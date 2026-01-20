package kr.co.kosmo.project_back.admin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import kr.co.kosmo.project_back.admin.dto.CartDto;

@Mapper
public interface AdminOrderItemMapper {
    List<CartDto> findOrderItemsByOrderId(Integer orderId);
}







