package kr.co.kosmo.project_back.order.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kosmo.project_back.address.mapper.AddressMapper;
import kr.co.kosmo.project_back.cart.dto.CartDto;
import kr.co.kosmo.project_back.cart.mapper.CartMapper;
import kr.co.kosmo.project_back.order.dto.OrderRequestDto;
import kr.co.kosmo.project_back.order.dto.OrderResponseDto;
import kr.co.kosmo.project_back.order.dto.OrderSearchDto;
import kr.co.kosmo.project_back.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;
    private final AddressMapper addressMapper;

    // 주문 생성
    @Transactional
    public Integer order(OrderRequestDto dto) {
        // 1. 배송지 처리 (기존 로직 유지 또는 아래 3번 로직 적용)
        processAddress(dto); 

        // 2. 주문할 상품 목록 (항상 프론트에서 보낸 items를 기준으로 함)
        List<CartDto> orderItems = dto.getItems();
        
        if(orderItems == null || orderItems.isEmpty()) {
            throw new IllegalStateException("주문할 상품이 없습니다.");
        }

        // 3. 결제 금액 계산
        int totalPrice = orderItems.stream()
            .mapToInt(i -> i.getPrice() * i.getQuantity())
            .sum();
        dto.setPaymentPrice(totalPrice);

        // 4. 주문 및 아이템 생성
        orderMapper.insertOrder(dto);
        Integer orderId = dto.getOrderId();
        orderMapper.insertOrderItems(orderId, orderItems);

        // 5. 장바구니 부분 삭제 (주문한 상품만!)
        if(Boolean.TRUE.equals(dto.getUseCartItems())) {
            for (CartDto item : orderItems) {
                // productId를 기준으로 장바구니에서 해당 상품만 제거
                cartMapper.deleteCartItem(dto.getUserId(), item.getProductId());
            }
        }
        return orderId;
    }  
    // 구매목록 조회(페이징)
    public Map<String, Object> getOrderList(OrderSearchDto searchDto) {
        // 전체 주문 개수
        int totalCount = orderMapper.countOrderList(searchDto);
        // 주문 목록 조회
        List<OrderResponseDto> orders = orderMapper.findOrderList(searchDto);
        // 주문 상세 조회
        for(OrderResponseDto order : orders) {
            List<CartDto> orderItems = 
                orderMapper.findOrderItemsByOrderId(order.getOrderId());
            order.setOrderItems(orderItems);
        }
        List<Map<String, Object>> orderList = orders.stream()
            .map(o -> {
                Map<String, Object> m = new java.util.HashMap<>();
                m.put("id", "ORD-" + String.format("%03d", o.getOrderId()));
                m.put("totalPrice", o.getTotalPrice());
                m.put("status", "결제 완료");
                m.put("orderDate", o.getCreatedAt());

                List<CartDto> items = 
                    orderMapper.findOrderItemsByOrderId(o.getOrderId());
                m.put("orderItems", items);

                return m;
            }).toList();

            return Map.of(
                "orders", orderList,
                "pagination", Map.of(
                    "totalCount", totalCount
            )
        );
    }

    private void processAddress(OrderRequestDto dto) {
        if (dto.getShippingAddressId() == null) {
            throw new IllegalStateException("배송지를 선택해주세요.");
        }
        dto.setAddressId(dto.getShippingAddressId());
    }
}
