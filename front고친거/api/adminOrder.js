import axiosInstance from "@/api/axiosInstance";

const BASE = "/admin/order";


// 주문 목록 조회  (검색 및 페이징)
export const getAdminOrderApi = (params) => {
    return axiosInstance.get(`${BASE}`, { params });
};

// 주문 상태 업데이트
export const updateOrderStatusApi = (orderId, status) => {
    // @PutMapping("/{orderId}/status") 형식이므로 URL에 orderId를 넣고, 
    // status는 @RequestParam이므로 params 객체로 전달
    return axiosInstance.put(`${BASE}/${orderId}/status`, null, {
        params: {status}
    });
};