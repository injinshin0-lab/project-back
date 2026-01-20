// src/routes/AppRouter.jsx
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import ProtectedRoute from "@/routes/ProtectedRoute";
import AdminRoute from "@/routes/AdminRoute";

// ✅ 레이아웃
import AppLayout from "@/components/layout/AppLayout";
import MyPageLayout from "@/components/layout/MyPageLayout";

// ✅ 메인/기본
import BogamMainPage from "@/pages/BogamMainPage";
import FaqPage from "@/pages/FaqPage";

// ✅ 인증 (로그인 1개로 통합)
import LoginForm from "@/components/auth/LoginForm";
import SignupPage from "@/pages/auth/SignupPage";

// ✅ 상품
import ProductsPage from "@/pages/products/ProductsPage";
import ProductDetailPage from "@/pages/products/ProductDetailPage";
import ProductReviewWritePage from "@/pages/reviews/ProductReviewWritePage";

// ✅ 장바구니/주문/결제
import CartPage from "@/pages/cart/CartPage";
import OrderPage from "@/pages/order/OrderPage";
import OrderCompletePage from "@/pages/order/OrderCompletePage";
import PaymentPage from "@/pages/payment/PaymentPage";
import PaymentFailPage from "@/pages/payment/PaymentFailPage";

// ✅ 마이페이지
import ProfilePage from "@/pages/mypage/ProfilePage";
import OrdersPage from "@/pages/order/OrdersPage";
import OrderDetailPage from "@/pages/mypage/orders/OrderDetailPage";
import AddressesPage from "@/pages/mypage/AddressesPage";
import MyReviewsPage from "@/pages/mypage/MyReviewsPage";
import NotificationsPage from "@/pages/mypage/NotificationsPage";
import NotificationSettingsPage from "@/pages/mypage/NotificationSettingsPage";

// ✅ 관리자
import AdminDashboard from "@/pages/admin/AdminDashboard";
import AdminProductsPage from "@/pages/admin/AdminProductsPage";
import AdminCategoriesPage from "@/pages/admin/AdminCategoriesPage";
import AdminUsersPage from "@/pages/admin/AdminUsersPage";
import AdminQnaPage from "@/pages/admin/AdminQnaPage";
import AdminNotificationsPage from "@/pages/admin/AdminNotificationsPage";
import AdminAlarmPage from "@/pages/admin/AdminAlarmPage";
import AdminFaqsPage from "@/pages/admin/AdminFaqsPage";
import AdminOrderPage from "@/pages/admin/AdminOrderPage";
import AdminLayout from "@/components/layout/AdminLayout";


export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        {/* 1) 일반 사용자 + 회원 경로 (헤더 포함) */}
        <Route element={<AppLayout />}>
          <Route path="/" element={<Navigate to="/bogam" replace />} />
          <Route path="/bogam" element={<BogamMainPage />} />
          <Route path="/faqs" element={<FaqPage />} />

          {/* ✅ 로그인 1개로 통합 */}
          <Route path="/login" element={<LoginForm />} />
          <Route path="/signup" element={<SignupPage />} />

          {/* 상품 */}
          <Route path="/products" element={<ProductsPage />} />
          <Route path="/products/:productId" element={<ProductDetailPage />} />

          {/* 회원 전용 (ProtectedRoute) */}
          <Route element={<ProtectedRoute />}>
            <Route path="/carts" element={<CartPage />} />
            <Route path="/order" element={<OrderPage />} />
            <Route path="/order/complete" element={<OrderCompletePage />} />

            <Route path="/payment" element={<PaymentPage />} />
            <Route path="/payment/fail" element={<PaymentFailPage />} />

            {/* 리뷰 작성 */}
            <Route
              path="/products/:productId/review/write"
              element={<ProductReviewWritePage />}
            />

            {/* 마이페이지 */}
            <Route path="/mypage" element={<MyPageLayout />}>
              <Route index element={<Navigate to="/mypage/profile" replace />} />
              <Route path="profile" element={<ProfilePage />} />
              <Route path="orders" element={<OrdersPage />} />
              <Route path="orders/:orderId" element={<OrderDetailPage />} />
              <Route path="addresses" element={<AddressesPage />} />
              <Route path="reviews" element={<MyReviewsPage />} />
              <Route path="notifications" element={<NotificationsPage />} />
              <Route
                path="notifications/settings"
                element={<NotificationSettingsPage />}
              />
            </Route>
          </Route>
        </Route>

        {/* ✅ 2) 관리자 로그인 페이지는 이제 없음 → /login으로 보냄 */}
        <Route path="/admin/login" element={<Navigate to="/login" replace />} />

        {/* 3) 관리자 경로 (role 기반 가드) */}
        <Route element={<AdminRoute />}>
          <Route element={<AdminLayout />}> {/* ✅ 레이아웃 추가 */}
            <Route path="/admin" element={<AdminDashboard />} />
            <Route path="/admin/products" element={<AdminProductsPage />} />
            <Route path="/admin/categories" element={<AdminCategoriesPage />} />
            <Route path="/admin/users" element={<AdminUsersPage />} />
            <Route path="/admin/qna" element={<AdminQnaPage />} />
            {/* <Route path="/admin/notifications" element={<AdminNotificationsPage />} /> */}
            <Route path="/admin/faqs" element={<AdminFaqsPage />} />
            <Route path="/admin/order" element={<AdminOrderPage />} />
            <Route path="/admin/alarm" element={<AdminAlarmPage />} />
          </Route>
        </Route>

        {/* 404 */}
        <Route path="*" element={<Navigate to="/bogam" replace />} />
      </Routes>
    </BrowserRouter>
  );
}