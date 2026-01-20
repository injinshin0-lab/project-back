// src/api/adminDashboardApi.js
// import axiosInstance from "@/api/axiosInstance";

/**
 * 관리자 대시보드 요약 통계
 * (임시 경로) GET /api/v1/admin/dashboard/summary
 *
 * 응답 예시:
 * {
 *   totalUsers: number,
 *   totalProducts: number,
 *   totalOrders: number,
 *   totalSales: number
 * }
 */
// export const getAdminDashboardSummaryApi = () =>
//   axiosInstance.get("/admin/dashboard");


import axiosInstance from "@/api/axiosInstance";

// 호출하는 쪽에서 .data를 또 붙일 필요 없게 함
export const getAdminDashboardSummaryApi = async () => {
  try {
    const response = await axiosInstance.get("/admin/dashboard");
    // 백엔드가 { status, data: {...} } 형태면 response.data.data
    // 백엔드가 { totalSales... } 형태면 response.data
    return response.data?.data || response.data;
  } catch (error) {
    console.error('대시보드 API 에러:', error);
    throw error;
  }
};