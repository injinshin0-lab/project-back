package kr.co.kosmo.project_back.user.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.CartDto;
import kr.co.kosmo.project_back.admin.dto.OrderResponseDto;
import kr.co.kosmo.project_back.admin.dto.OrderSearchDto;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.admin.mapper.AdminOrderItemMapper;
import kr.co.kosmo.project_back.user.dto.OrderRequestDto;
import kr.co.kosmo.project_back.user.mapper.CartMapper;
import kr.co.kosmo.project_back.user.mapper.OrderMapper;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;
    private final AdminOrderItemMapper orderItemMapper;

    public Integer order(OrderRequestDto dto) {
        // 주문 생성
        Integer result = orderMapper.insertOrder(dto);
        if (result > 0 && dto.getOrderId() != null && dto.getCartItems() != null) {
            // 주문 항목 추가
            orderMapper.insertOrderItems(dto.getOrderId(), dto.getCartItems());
            // 장바구니 비우기
            cartMapper.deleteCart(dto.getUserId());
        }
        return result;
    }

    public PageResponseDto<OrderResponseDto> getOrderList(OrderSearchDto searchDto) {
        Integer totalCount = orderMapper.countOrderList(searchDto);
        Integer totalPage = totalCount > 0 ? (int) Math.ceil((double) totalCount / searchDto.getSize()) : 1;
        
        List<OrderResponseDto> orderList = orderMapper.findOrderList(searchDto);
        
        // 각 주문에 주문 항목 리스트 추가
        for (OrderResponseDto order : orderList) {
            List<CartDto> orderItems = orderItemMapper.findOrderItemsByOrderId(order.getId());
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
}

