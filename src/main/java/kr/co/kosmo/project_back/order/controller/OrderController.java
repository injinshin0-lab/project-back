package kr.co.kosmo.project_back.order.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.order.dto.OrderRequestDto;
import kr.co.kosmo.project_back.order.dto.OrderSearchDto;
import kr.co.kosmo.project_back.order.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // 주문 생성
    @PostMapping
    public ResponseEntity<?> order(@RequestBody OrderRequestDto dto, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("LOGIN_USER");
        if(userId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        dto.setUserId(userId);
        Integer orderPk = orderService.order(dto);
        return ResponseEntity.ok(
            java.util.Map.of(
                "orderId", "ORD-" + String.format("%03d", orderPk),
                "paymentRequired", true,
                "paymentUrl", "/payment/" + orderPk
            )
        );
    }
    // 주문 / 구매 내역 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getOrderList(
        OrderSearchDto searchDto, HttpSession session) {
        // 로그인 체크
        Integer userId = (Integer) session.getAttribute("LOGIN_USER");
        if(userId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        // 기본 페이징 값 처리
        if(searchDto.getPage() == null) {
            searchDto.setPage(1);
        }
        if(searchDto.getSize() == null) {
            searchDto.setSize(10);
        }
        // 사용자 주입
        searchDto.setUserId(userId);
        // 서비스 호출
        Map<String, Object> result = orderService.getOrderList(searchDto);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Integer orderId, @RequestBody Map<String, String> body) {
        orderService.updateStatus(orderId, body.get("status"));
        return ResponseEntity.ok().build();
    }
}
