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
        // 기존 배송지를 선택한 경우
         if (dto.getShippingAddressId() != null) {
            dto.setAddressId(dto.getShippingAddressId());
            return;
        }
        // 신규 주소를 입력한 경우
        if (dto.getPostcode() != null && !dto.getPostcode().isEmpty()) {
            // 주소 객체 생성 및 정보 매핑
            // AddressDto의 필드명은 프로젝트의 Bg_Address 테이블 구조에 맞게 조정하세요.
            kr.co.kosmo.project_back.address.dto.AddressDto newAddr = new kr.co.kosmo.project_back.address.dto.AddressDto();
            newAddr.setUserId(dto.getUserId());
            newAddr.setPostcode(dto.getPostcode());
            newAddr.setAddress(dto.getAddress1());
            newAddr.setDetailAddress(dto.getAddress2());
            newAddr.setRecipient(dto.getRecipient()); // 필요 시 프론트에서 받아오세요
            newAddr.setRecipientPhone(dto.getRecipientPhone()); // 필요 시 프론트에서 받아오세요
            newAddr.setIsDefault(0); // 신규 주소는 기본 배송지 아님으로 설정
    
            // 1. DB에 주소 저장 (Mapper에서 selectKey를 통해 ID를 받아와야 함)
            addressMapper.insertAddress(newAddr); 
            
            // 2. 저장된 신규 주소 ID를 주문 DTO에 세팅
            dto.setAddressId(newAddr.getId()); 
            return;
        }
    
        // 3. 둘 다 없는 경우에만 예외 발생
        throw new IllegalStateException("배송지를 선택하거나 신규 주소를 입력해주세요.");

    }

    // 마이페이지 주문 조회
    public Map<String, Object> getOrderListByUserId(Integer userId) {
        List<OrderResponseDto> orders = 
            orderMapper.findOrderListByUserId(userId);
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
            })
            .toList();
            return Map.of(
                "orders", orderList,
                "pagination", Map.of(
                        "totalCount", orders.size()
                )
            );
        }
    }
