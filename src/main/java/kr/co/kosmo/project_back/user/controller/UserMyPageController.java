package kr.co.kosmo.project_back.user.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.order.service.OrderService;
import kr.co.kosmo.project_back.user.dto.UserPasswordChangeRequestDto;
import kr.co.kosmo.project_back.user.dto.UserUpdateRequestDto;
import kr.co.kosmo.project_back.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserMyPageController {

    private final OrderService orderService;
    private final UserService userService;

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateMyInfo(
        @PathVariable Integer userId,
        @RequestBody UserUpdateRequestDto dto,
        HttpSession session
    ) {
        // 로그인 여부 확인
        Integer loginUserId = (Integer) session.getAttribute("LOGIN_USER");
        if(loginUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("로그인이 필요합니다.");
        }
        // 본인 정보만 수정 가능
        if(!loginUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("본인 정보만 수정가능합니다.");
        }
        // 회원 정보 수정
        userService.updateUserInfo(userId, dto);
        // 성공 응답
        return ResponseEntity.ok(
            Map.of("message", "회원 정보가 수정되었습니다.")
        );
    }

    @PatchMapping("/{userId}/password-change")
    public ResponseEntity<?> updatePasswordByUserId(
        @PathVariable Integer userId,
        @RequestBody UserPasswordChangeRequestDto dto,
        HttpSession session
    ) {
        // 로그인 여부 확인
        Integer loginUserId = (Integer) session.getAttribute("LOGIN_USER");
        if(loginUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("로그인이 필요합니다.");
        }
        // 본인 정보만 수정 가능
        if(!loginUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("본인 비밀번호만 변경 가능합니다.");
        }
        // 서비스 호출
        userService.changePassword(userId, dto);
        // 성공
        return ResponseEntity.ok(
                Map.of("message", "비밀번호가 성공적으로 변경되었습니다.")
        );
    }
    // 마이페이지 주문 내역 조회
    @GetMapping("/orders/user/{userId}")
    public ResponseEntity<?> getMyOrders(
        @PathVariable Integer userId,
        HttpSession session
    ) {
        // 로그인 체크
        Integer loginUserId = (Integer) session.getAttribute("LOGIN_USER");
        if(loginUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("로그인이 필요합니다.");
        }
        // 본인만 조회 가능
        if(!loginUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("본인 주문 내역만 조회 가능합니다.");
        }
        return ResponseEntity.ok(
               orderService.getOrderListByUserId(loginUserId)
        );
    }
}
