package kr.co.kosmo.project_back.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.kosmo.project_back.cart.dto.CartDto;
import kr.co.kosmo.project_back.order.dto.OrderRequestDto;
import kr.co.kosmo.project_back.order.dto.OrderResponseDto;
import kr.co.kosmo.project_back.order.dto.OrderSearchDto;

@Mapper
public interface OrderMapper {
    // 주문 생성
    void insertOrder(OrderRequestDto dto);
    // 주문 상품 여러개 생성
    void insertOrderItems(
        @Param("orderId") Integer orderId,
        @Param("items") List<CartDto> items
    );
    // 주문 내역 개수 조회(페이징)
    int countOrderList(OrderSearchDto searchDto);
    // 주문 / 구매 내역 조회
    List<OrderResponseDto> findOrderList(OrderSearchDto searchDto);
    // 주문 상세 상품 조회
    List<CartDto> findOrderItemsByOrderId(Integer orderId);
}
