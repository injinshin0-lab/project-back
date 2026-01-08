package kr.co.kosmo.project_back.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.user.dto.ProductDto;
import kr.co.kosmo.project_back.user.service.WishlistService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getWishList(@RequestParam Integer userId) {
        return ResponseEntity.ok(wishlistService.getWishList(userId));
    }

    @PostMapping("/toggle")
    public ResponseEntity<Integer> toggleWish(
            @RequestParam Integer userId,
            @RequestParam Integer productId) {
        return ResponseEntity.ok(wishlistService.toggleWish(userId, productId));
    }
}

