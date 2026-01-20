package kr.co.kosmo.project_back.admin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.kosmo.project_back.admin.dto.OrderResponseDto;
import kr.co.kosmo.project_back.admin.dto.OrderSearchDto;

@Mapper
public interface AdminOrderMapper {
    Integer countOrderList(OrderSearchDto searchDto);
    List<OrderResponseDto> findOrderList(OrderSearchDto searchDto);
    Integer updateOrderStatus(@Param("orderId") Integer orderId, @Param("status") String status);
    OrderResponseDto findOrderById(Integer orderId);
}

