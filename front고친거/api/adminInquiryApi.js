// src/api/adminInquiryApi.js
import axiosInstance from "@/api/axiosInstance";

/**
 * ✅ 관리자 문의(Inquiry) API (표 기준)
 * - GET   /api/v1/admin/inquiries              (params: keyword, status, page, size)
 * - PATCH /api/v1/admin/inquiries/{id}/reply   (body: { reply })
 */
const BASE = "/admin/inquiries";

export const adminGetInquiriesApi = (params = {}) =>
  axiosInstance.get(BASE, { params });

export const adminReplyInquiryApi = (inquiriesId, payload) =>
  axiosInstance.patch(`${BASE}/${inquiriesId}/reply`, payload);