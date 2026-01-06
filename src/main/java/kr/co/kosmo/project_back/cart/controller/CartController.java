package kr.co.kosmo.project_back.cart.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.cart.dto.CartDto;
import kr.co.kosmo.project_back.cart.service.CartService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    // 장바구니 조회
    @GetMapping
    public Map<String, Object> getCartList(HttpSession session) {
        Integer userId = 
            (Integer) session.getAttribute("LOGIN_USER");
        // 해당 사용자 장바구니 목록 조회
        List<CartDto> cartItems = 
            cartService.getCartList(userId);
        // 장바구니 총 금액 계산
        int totalPrice = 
            cartItems.stream().mapToInt(item -> item.getPrice() * item.getQuantity()).sum();
        return Map.of(
                "cartItems", cartItems,
                "totalPrice", totalPrice
        );
    }
    // 장바구니 추가
    @PostMapping
    public Map<String, String> addCartItem(
        @RequestBody CartDto dto,
        HttpSession session
    ) {
        Integer userId = 
            (Integer) session.getAttribute("LOGIN_USER");
        dto.setUserId(userId);
        cartService.addCartItem(dto);
        return Map.of(
                "message",
                "장바구니에 추가되었습니다."
        );
    }
}
