package kr.co.kosmo.project_back.wishlist.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.product.dto.ProductDto;
import kr.co.kosmo.project_back.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    // 찜 목록 조회
    @GetMapping
    public List<ProductDto> getWishList(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("LOGIN_USER");
        if(userId == null ) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        return wishlistService.getWishList(userId);
    }
    // 찜 목록 추가
    @PostMapping("/{productId}")
    public List<ProductDto> toggleWishAdd(HttpSession session, @PathVariable Integer productId) {
        Integer userId = (Integer) session.getAttribute("LOGIN_USER");
        if(userId == null ) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        return wishlistService.toggleWish(userId, productId);
    }
    // 찜 목록 삭제
    @DeleteMapping("/{productId}")
    public List<ProductDto> toggleWishDelete(HttpSession session, @PathVariable Integer productId) {
        Integer userId = (Integer) session.getAttribute("LOGIN_USER");
        if(userId == null ) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        return wishlistService.toggleWish(userId, productId);
    }

    
}
