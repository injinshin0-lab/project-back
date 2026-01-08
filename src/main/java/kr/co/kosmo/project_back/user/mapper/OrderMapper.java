package kr.co.kosmo.project_back.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.kosmo.project_back.admin.dto.CartDto;
import kr.co.kosmo.project_back.admin.dto.OrderSearchDto;
import kr.co.kosmo.project_back.admin.dto.OrderResponseDto;
import kr.co.kosmo.project_back.user.dto.OrderRequestDto;

@Mapper
public interface OrderMapper {
    Integer insertOrder(OrderRequestDto dto);
    Integer insertOrderItems(@Param("orderId") Integer orderId, @Param("items") List<CartDto> items);
    Integer countOrderList(OrderSearchDto searchDto);
    List<OrderResponseDto> findOrderList(OrderSearchDto searchDto);
}

