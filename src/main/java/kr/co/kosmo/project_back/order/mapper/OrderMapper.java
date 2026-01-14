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
    // 마이페이지 주문 내역 조회
    List<OrderResponseDto> findOrderListByUserId(Integer userId);
    // 주문 상세 상품 조회
    List<CartDto> findOrderItemsByOrderId(Integer orderId);
    // 리뷰 작성 가능 여부 확인(구매완료여부)
    int countCompletedOrder(
        @Param("userId") Integer usdrId,
        @Param("productId") Integer productId 
    );
}
