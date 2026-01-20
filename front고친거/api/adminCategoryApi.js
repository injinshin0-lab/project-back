// src/api/adminCategoryApi.js
import axiosInstance from "@/api/axiosInstance";

/**
 * ✅ 카테고리 CRUD (CategoryController 기반)
 * - GET    /api/v1/categories
 * - POST   /api/v1/categories
 * - PUT    /api/v1/categories/{categoryId}
 * - DELETE /api/v1/categories/{categoryId}
 */
const BASE = "/admin/category";

export const adminGetCategoriesApi = () => axiosInstance.get(BASE);

export const adminCreateCategoryApi = (payload) =>
  axiosInstance.post(BASE, payload);

export const adminUpdateCategoryApi = (payload) =>
  axiosInstance.put(BASE, payload);

export const adminDeleteCategoryApi = (categoryId) =>
  axiosInstance.delete(`${BASE}?id=${categoryId}`);