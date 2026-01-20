// src/pages/admin/AdminDashboard.jsx
import { useEffect, useState } from "react";
import toast from "react-hot-toast";

import { getAdminDashboardSummaryApi } from "@/api/adminDashboardApi";

const Card = ({ title, value }) => (
  <div
    style={{
      border: "1px solid #ddd",
      borderRadius: 12,
      padding: 16,
      background: "#fff",
    }}
  >
    <div style={{ fontSize: 14, color: "#666", fontWeight: 700 }}>
      {title}
    </div>
    <div style={{ marginTop: 8, fontSize: 28, fontWeight: 900 }}>
      {value}
    </div>
  </div>
);

export default function AdminDashboard() {
  const [loading, setLoading] = useState(false);
  const [summary, setSummary] = useState({
    totalUsers: 0,
    totalProducts: 0,
    totalOrders: 0,
    totalSales: 0,
  });

  const fetchSummary = async () => {
    try {
      setLoading(true);
      const data = await getAdminDashboardSummaryApi();

      console.log("대시보드 원본 데이터:", data);
      setSummary(data); 
    } catch (e) {
      toast.error("대시보드 통계 조회 실패");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSummary();
  }, []);

  return (
    <div style={{ padding: 20 }}>
      <h2 style={{ fontSize: 22, fontWeight: 900, marginBottom: 16 }}>
        관리자 대시보드
      </h2>
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(4, 1fr)",
          gap: 16,
        }}
      >
        {/* <Card
          title="총 회원 수"
          value={loading ? "…" : summary.totalUsers.toLocaleString()}
        />
        <Card
          title="총 상품 수"
          value={loading ? "…" : summary.totalProducts.toLocaleString()}
        />
        <Card
          title="총 주문 수"
          value={loading ? "…" : summary.totalOrders.toLocaleString()}
        />
        <Card
          title="총 판매 금액"
          value={
            loading
              ? "…"
              : `${summary.totalSales.toLocaleString()}원`
          }
        /> */}
        <Card
          title="총 회원 수"
          value={loading ? "…" : (summary?.totalUserCount?.toLocaleString() ?? "0")}
        />
        <Card
          title="총 상품 수"
          value={loading ? "…" : (summary?.totalProductCount?.toLocaleString() ?? "0")}
        />
        <Card
          title="총 주문 수"
          value={loading ? "…" : (summary?.totalOrderCount?.toLocaleString() ?? "0")}
        />
        <Card
          title="총 판매 금액"
          value={loading ? "…" : `${(summary?.totalSalesAmount?.toLocaleString() ?? "0")}원`}
        />

      </div>
    </div>
  );
} 