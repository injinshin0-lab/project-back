package kr.co.kosmo.project_back.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.OrderResponseDto;
import kr.co.kosmo.project_back.admin.dto.OrderSearchDto;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.admin.service.AdminOrderService;

@RestController
@RequestMapping("/api/v1/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {
    private final AdminOrderService orderService;

    @GetMapping
    public ResponseEntity<PageResponseDto<OrderResponseDto>> getOrderList(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        OrderSearchDto searchDto = new OrderSearchDto();
        searchDto.setUserId(userId);
        searchDto.setStartDate(startDate);
        searchDto.setEndDate(endDate);
        searchDto.setPage(page);
        searchDto.setSize(size);
        
        return ResponseEntity.ok(orderService.getOrderList(searchDto));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Integer> updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }
}







