// src/api/adminFaqApi.js
import axiosInstance from "@/api/axiosInstance";

/**
 * ✅ 관리자 FAQ API (스펙/다이어그램 기준)
 * - GET    /api/v1/admin/faqs        (params: keyword, page, size)
 * - POST   /api/v1/admin/faqs
 * - PUT    /api/v1/admin/faqs/{faqId}
 * - DELETE /api/v1/admin/faqs/{faqId}
 */
const BASE = "/admin/faqs";

export const adminGetFaqsApi = (params = {}) =>
  axiosInstance.get(BASE, { params });

export const adminCreateFaqApi = (payload) =>
  axiosInstance.post(BASE, payload);

export const adminUpdateFaqApi = (faqId, payload) => 
  axiosInstance.put(`${BASE}/${faqId}`, payload);

export const adminDeleteFaqApi = (faqId) =>
  axiosInstance.delete(`${BASE}/${faqId}`);