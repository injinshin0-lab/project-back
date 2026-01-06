package kr.co.kosmo.project_back.cart.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        Integer userId = (Integer) session.getAttribute("LOGIN_USER");
        if(userId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        // 해당 사용자 장바구니 목록 조회
        List<CartDto> cartItems = cartService.getCartList(userId);
        
        // 장바구니 총 금액 계산
        int totalPrice = cartItems.stream()
            .mapToInt(item -> item.getPrice() * item.getQuantity())
            .sum();
        List<Map<String, Object>> responseItems = cartItems.stream()
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("cartItemId", item.getCartItemId());
                    map.put("productId", item.getProductId());
                    map.put("productName", item.getProductName());
                    map.put("quantity", item.getQuantity());
                    map.put("price", item.getPrice());
                    return map;
                })
                .collect(Collectors.toList());
        return Map.of(
                "cartItems", responseItems,
                "totalPrice", totalPrice
        );
    }
    // 장바구니 추가
    @PostMapping
    public Map<String, Object> addCartItem(@RequestBody CartDto dto, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("LOGIN_USER");
        if(userId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        dto.setUserId(userId);
        cartService.addCartItem(dto);
        return Map.of("message", "장바구니에 추가되었습니다.");
    }
    // 수량 수정
    @PatchMapping("/{cartId}/quantity")
    public Map<String, Object> addCartItem(
            @PathVariable Integer cartId,
            @RequestBody Map<String, Integer> body,
            HttpSession session
    ) {
        Integer userId = (Integer) session.getAttribute("LOGIN_USER");
        if (userId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        CartDto dto = new CartDto();
        dto.setCartItemId(cartId);
        dto.setUserId(userId);
        dto.setQuantity(body.get("quantity"));
        cartService.updateCartQuantity(dto);
        return Map.of(
            "message", "징바구니에 수량 수정됨",
            "cartItemId", cartId);
    }
}
