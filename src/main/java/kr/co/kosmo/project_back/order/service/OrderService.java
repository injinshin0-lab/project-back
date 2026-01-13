package kr.co.kosmo.project_back.order.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.cart.dto.CartDto;
import kr.co.kosmo.project_back.cart.mapper.CartMapper;
import kr.co.kosmo.project_back.order.dto.OrderRequestDto;
import kr.co.kosmo.project_back.order.dto.OrderResponseDto;
import kr.co.kosmo.project_back.order.dto.OrderSearchDto;
import kr.co.kosmo.project_back.order.mapper.OrderMapper;
import kr.co.kosmo.project_back.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    // 주문 생성
    public Integer order(OrderRequestDto dto) {
        // 주문 1건 생성
        dto.setAddressId(dto.getShippingAddressId());   // orderId 생성됨
        // 주문할 상품 목록 결정
        List<CartDto> orderItems;
        if(Boolean.TRUE.equals(dto.getUseCartItems())) {
            // 장바구니 주문
            orderItems = cartMapper.findCartListByUserId(dto.getUserId());
        } else {
            // 바로구매
            orderItems = dto.getItems();
        }
        // 주문 상품 없을때 방어
        if(orderItems == null || orderItems.isEmpty()) {
            throw new IllegalStateException("주문할 상품이 없습니다.");
        }
        for(CartDto item : orderItems) {
            if(item.getPrice() == null) {
                Integer price = productMapper.findPriceByProductId(item.getProductId());
                item.setPrice(price);
            }
        }
        // 총 결제 금액 계산
        int totalPrice = orderItems.stream()
            .mapToInt(i -> i.getPrice() * i.getQuantity())
            .sum();
        dto.setPaymentPrice(totalPrice);
        // 주문 생성
        orderMapper.insertOrder(dto);
        Integer orderId = dto.getOrderId();
        // 주문 상품 저장
        orderMapper.insertOrderItems(orderId, orderItems);
        // 장바구니 비우기
        if(Boolean.TRUE.equals(dto.getUseCartItems())) {
            cartMapper.deleteCart(dto.getUserId());
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
    }
