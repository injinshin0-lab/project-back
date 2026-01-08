package kr.co.kosmo.project_back.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.OrderResponseDto;
import kr.co.kosmo.project_back.admin.dto.OrderSearchDto;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.user.dto.OrderRequestDto;
import kr.co.kosmo.project_back.user.service.OrderService;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Integer> order(@RequestBody OrderRequestDto dto) {
        return ResponseEntity.ok(orderService.order(dto));
    }

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
}

