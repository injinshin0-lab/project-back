// src/api/adminProductApi.js
import axiosInstance from "@/api/axiosInstance";

/**
 * ✅ 관리자 상품 API (Spring Boot 스펙 기준)
 * - GET    /api/v1/admin/products
 * - POST   /api/v1/admin/products           (multipart)
 * - PATCH  /api/v1/admin/products/{productId} (multipart or json)
 * - DELETE /api/v1/admin/products/{productId}
 *
 * axiosInstance baseURL = /api/v1 가정 -> 여기서는 /admin/products 만 사용
 */
const BASE = "/admin/products";

export const adminGetProductsApi = (params = {}) =>
  axiosInstance.get(BASE, { params,
    paramsSerializer: (params) => {
        const searchParams = new URLSearchParams();
        for (const key in params) {
          const value = params[key];
          if (Array.isArray(value)) {
            // 배열이면 categoryIds=1&categoryIds=2 형태로 반복 추가
            value.forEach(v => searchParams.append(key, v));
          } else if (value !== undefined && value !== null) {
            searchParams.append(key, value);
          }
        }
      return searchParams.toString();
    }
  });

export const adminGetProductDetailApi = (productId) =>
  axiosInstance.get(`${BASE}/${productId}`);

export const adminDeleteProductApi = (productId) =>
  axiosInstance.delete(`${BASE}/${productId}`);

// --------------------
// ✅ multipart helpers
// --------------------
function appendIf(formData, key, value) {
  if (value === undefined || value === null) return;
  if (typeof value === "string" && value.trim() === "") return;
  formData.append(key, value);
}

export function buildAdminProductFormData(payload = {}) {
  /**
   * - 여기서는 가장 흔한 필드들로 formData를 만들고
   * - 서버가 categoryId 대신 category.categoryId 같은 구조를 원하면 여기만 바꾸면 됨
   */
  const fd = new FormData();

  appendIf(fd, "productName", payload.productName ?? payload.name);
  appendIf(fd, "originName", payload.originName);
  appendIf(fd, "content", payload.content);
  appendIf(fd, "price", payload.price);

  const targetIds = payload.categoryIds || payload.categoryId;

  if (Array.isArray(targetIds) && targetIds.length > 0) {
    console.log("보낼 배열 데이터:", targetIds); 
    
    targetIds.forEach(id => {
      fd.append("categoryIds", id); 
    });
  }

  // ✅ 이미지 파일
  const file = payload.imageFile;
  
  if (file instanceof File) {
    console.log("이미지 파일 감지됨:", file.name);
    fd.append("imageFile", file);
  } else {
    console.warn("이미지 파일이 File 객체가 아닙니다:", file);
  }

  // 4. 이미지 URL (수정 시 기존 이미지 유지용)
  appendIf(fd, "imageUrl", payload.imageUrl);

  return fd;
}

// --------------------
// ✅ create / update
// --------------------

export const adminCreateProductApi = async (payload) => {
  const fd = buildAdminProductFormData(payload);
  // 등록은 단순 POST이며, /products 주소까지만 보내야 합니다.
  const response = await axiosInstance.post(BASE, fd, {
    headers: { "Content-Type": "multipart/form-data" },
  });
  return response.data;
};

export const adminUpdateProductApi = (productId, payload) => {
  // 수정도 imageFile 있으면 multipart, 없으면 json으로 보내도 되지만
  // 백엔드가 multipart만 받는 경우가 많아서 "항상 multipart"로 통일
  const fd = buildAdminProductFormData(payload);
  return axiosInstance.patch(`${BASE}/${productId}`, fd, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};