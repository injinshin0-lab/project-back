import axiosInstance from "@/api/axiosInstance";

const BASE = "/admin/users";

// 회원 목록 조회 (검색/페이징)
/**
* @param {Object} params - { page, size, startDate, endDate, keywordType, keyword }
 */

export const getAdminUsersApi = (params) => {
    return axiosInstance.get(BASE, {params});
};

// 회원 상세 정보 조회

/**
* @param {number} userId - 회원의 고유 ID (PK)
*/

export const getAdminUserDetailApi = (userId) => {
    return axiosInstance.get(`${BASE}/${userId}`);
};

/**
 * 3. (참고) 관리자 알림 발송 - 필요 시 주석 해제 후 사용
 * @param {number} userId 
 * @param {Object} data - { message: "알림 내용" }
 */
// export const sendAdminAlarmApi = (userId, data) => {
//   return axiosInstance.post(`${BASE}/${userId}/alarm`, data);
// };