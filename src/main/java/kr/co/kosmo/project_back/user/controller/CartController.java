package kr.co.kosmo.project_back.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.CartDto;
import kr.co.kosmo.project_back.user.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartDto>> getCartList(@RequestParam Integer userId) {
        return ResponseEntity.ok(cartService.getCartList(userId));
    }

    @PostMapping
    public ResponseEntity<Integer> addCartItem(@RequestBody CartDto dto) {
        return ResponseEntity.ok(cartService.addCartItem(dto));
    }

    @DeleteMapping("/item")
    public ResponseEntity<Integer> removeCartItem(@RequestBody CartDto dto) {
        return ResponseEntity.ok(cartService.removeCartItem(dto));
    }

    @DeleteMapping
    public ResponseEntity<Integer> removeCart(@RequestBody CartDto dto) {
        return ResponseEntity.ok(cartService.removeCart(dto));
    }
}

