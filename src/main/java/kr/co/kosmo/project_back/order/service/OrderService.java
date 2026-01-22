package kr.co.kosmo.project_back.order.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kosmo.project_back.address.mapper.AddressMapper;
import kr.co.kosmo.project_back.alarm.dto.AlarmDto;
import kr.co.kosmo.project_back.alarm.dto.AlarmSettingDto;
import kr.co.kosmo.project_back.alarm.mapper.AlarmMapper;
import kr.co.kosmo.project_back.alarm.service.AlarmService;
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
    private final AlarmService alarmService; // 알림 서비스 주입 추가
    private final AlarmMapper alarmMapper;
    
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

        // ✅ [추가] 주문 완료 알림 생성 트리거
        // 주문 성공 후 DB에 알림 데이터를 쌓습니다.
        try {
            String orderNo = "ORD-" + String.format("%03d", orderId);
            alarmService.insertOrderAlarm(dto.getUserId(), orderNo);
        } catch (Exception e) {
            // 알림 생성 실패가 주문 전체의 롤백으로 이어지지 않도록 예외 처리만 수행
            System.err.println("알림 생성 실패: " + e.getMessage());
        }
        return orderId;
    }  

    @Transactional
    public void updateStatus(Integer orderId, String newStatus) {
        OrderResponseDto order = orderMapper.findOrderById(orderId);

        if (order == null) return;

        orderMapper.updateOrderStatus(orderId, newStatus);

        String orderNo = "ORD-" + String.format("%03d", orderId);

        String checkStatus = newStatus.replace(" ", "");

        int duplicateCount = orderMapper.checkDuplicateAlarm(order.getUserId(), orderNo, checkStatus);
        
        // 중복이 아닐 때만 알림 생성
        if (duplicateCount == 0) {
            AlarmSettingDto setting = alarmMapper.findSettingsByUserId(order.getUserId());

            // 배송 알림 설정이 OFF(false)라면 여기서 리턴하여 중단
            if (setting != null && Boolean.FALSE.equals(setting.getIsDeliveryEnabled())) {
                return; 
            }

            AlarmDto alarm = new AlarmDto();
            alarm.setUserId(order.getUserId());
            
            // 상태별로 타입을 세분화하거나, 타입은 DELIVERY로 두되 메시지를 다르게 구성
            alarm.setType("DELIVERY"); 
            
            String statusMsg = "";
            switch(newStatus) {
                case "결제 완료": 
                case "결제완료": 
                    statusMsg = "결제 완료되었습니다. 정상적으로 처리되었습니다."; // "결제 완료" 키워드 포함
                    break;
                case "상품 준비중": 
                case "상품준비중":
                    statusMsg = "상품 준비중입니다. 조금만 기다려주세요."; // "상품 준비중" 키워드 포함
                    break;
                case "배송 중": 
                case "배송중":
                    statusMsg = "배송 중입니다. 상품 배송이 시작되었습니다!"; // "배송 중" 키워드 포함
                    break;
                case "배송 완료": 
                case "배송완료":
                    statusMsg = "배송 완료되었습니다. 상품은 만족스러우신가요?"; // "배송 완료" 키워드 포함
                    break;
                case "주문취소": 
                    statusMsg = "주문 취소되었습니다. 이용해 주셔서 감사합니다."; // "주문 취소" 키워드 포함
                    break;
                default: 
                    statusMsg = "주문 상태가 [" + newStatus + "]로 변경되었습니다.";
            }
            
            alarm.setMessage(String.format("[%s] %s", orderNo, statusMsg));
            
            alarmMapper.insertAlarm(alarm);
        }
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
                m.put("status", o.getStatus());
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
                m.put("status", o.getStatus());
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

    public boolean canUserReview(Integer userId, Integer productId) {
        // mapper의 countCompletedOrder는 '결제 완료', '상품 준비중', '배송 중', '배송 완료'를 체크함
        int count = orderMapper.countCompletedOrder(userId, productId);
        return count > 0;
    }
}

    
