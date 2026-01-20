import axios from "axios";

const API_BASE_URL = "/api/v1/alarm";

// 1. 관리자가 알림 발송 (백엔드에 @PostMapping("/send") 구현 필요)
export const sendAlarmApi = async (alarmData) => {
    return await axios.post(`${API_BASE_URL}/send`, alarmData);
};

// 2. 유저별 알림 목록 조회 (기존과 동일)
export const getAlarmByUserIdApi = async (userId) => {
    return await axios.get(`${API_BASE_URL}?userId=${userId}`);
};

// 3. 알림 하나 읽음 처리 (추가)
export const markAsReadApi = async (notificationId) => {
    return await axios.put(`${API_BASE_URL}/read/${notificationId}`);
};

// 4. 모든 알림 읽음 처리 (추가)
export const markAllReadApi = async (userId) => {
    return await axios.put(`${API_BASE_URL}/read-all?userId=${userId}`);
};

// 5. 알림 삭제 (추가)
export const deleteAlarmApi = async (notificationId) => {
    return await axios.delete(`${API_BASE_URL}/${notificationId}`);
};

// 알람 10 페이지조회
export const getAllAlarmsApi = async (page = 1, size = 10) => {
    return await axios.get(`${API_BASE_URL}/all`,{
        params: {
            page:page,
            size: size
        }
    }); // 백엔드에 @GetMapping("/all") 구현 필요
};