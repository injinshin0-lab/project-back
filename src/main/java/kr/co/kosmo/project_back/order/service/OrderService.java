package kr.co.kosmo.project_back.order.service;

import org.springframework.stereotype.Service;

@Service
public class OrderService {
    // 구매 여부 확정 확인(임시)
    public boolean hasConfirmedPurchase(Long userId, Integer productId) {
        return true;        // 나중에 수정 필요 -> 지금은 항상 통과
    }
}
