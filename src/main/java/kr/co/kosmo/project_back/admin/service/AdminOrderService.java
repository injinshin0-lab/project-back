package kr.co.kosmo.project_back.admin.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.OrderResponseDto;
import kr.co.kosmo.project_back.admin.dto.OrderSearchDto;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.admin.mapper.AdminOrderMapper;
import kr.co.kosmo.project_back.admin.mapper.AdminOrderItemMapper;
import kr.co.kosmo.project_back.user.service.AlarmService;

@Service
@RequiredArgsConstructor
public class AdminOrderService {
    private final AdminOrderMapper orderMapper;
    private final AdminOrderItemMapper orderItemMapper;
    private final AlarmService alarmService;

    public PageResponseDto<OrderResponseDto> getOrderList(OrderSearchDto searchDto) {
        Integer totalCount = orderMapper.countOrderList(searchDto);
        Integer totalPage = (int) Math.ceil((double) totalCount / searchDto.getSize());
        
        List<OrderResponseDto> orderList = orderMapper.findOrderList(searchDto);
        
        // 각 주문에 주문 항목 리스트 추가
        for (OrderResponseDto order : orderList) {
            List<kr.co.kosmo.project_back.admin.dto.CartDto> orderItems = 
                orderItemMapper.findOrderItemsByOrderId(order.getId());
            // 이미지 URL에 uploads/ 접두사 추가
            orderItems.forEach(item -> {
                if (item.getImageUrl() != null && !item.getImageUrl().startsWith("/uploads/") && !item.getImageUrl().startsWith("uploads/")) {
                    item.setImageUrl("/uploads/" + item.getImageUrl());
                }
            });
            order.setOrderItems(orderItems);
        }
        
        PageResponseDto<OrderResponseDto> response = new PageResponseDto<>();
        response.setList(orderList);
        response.setTotalPage(totalPage);
        response.setCurrentPage(searchDto.getPage());
        
        return response;
    }
    String oldStatus = null;
    public Integer updateOrderStatus(Integer orderId, String status) {
        // 주문 정보 조회하여 사용자 ID 확인
        OrderResponseDto order = orderMapper.findOrderById(orderId);
        
        if (order != null) {
            oldStatus = order.getStatus();
            // 각 주문 항목 추가
            List<kr.co.kosmo.project_back.admin.dto.CartDto> orderItems = 
                orderItemMapper.findOrderItemsByOrderId(order.getId());
            // 이미지 URL에 uploads/ 접두사 추가
            orderItems.forEach(item -> {
                if (item.getImageUrl() != null && !item.getImageUrl().startsWith("/uploads/") && !item.getImageUrl().startsWith("uploads/")) {
                    item.setImageUrl("/uploads/" + item.getImageUrl());
                }
            });
            order.setOrderItems(orderItems);
        }
        
        Integer result = orderMapper.updateOrderStatus(orderId, status);
        
        if (result > 0 && order != null && order.getUserId() != null) {
            // 배송 상태 변경 알림 발송
            try {
                alarmService.insertDeliveryAlarm(order.getUserId(), orderId, status);
            } catch (Exception e) {
                // 알림 발송 실패해도 상태 업데이트는 성공으로 처리
                System.err.println("알림 발송 실패: " + e.getMessage());
            }
        }
        
        return result;
    }
}
