import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { getAdminOrderApi, updateOrderStatusApi } from "@/api/adminOrder";

export default function AdminOrderPage() {
    const [loading, setLoading] = useState(false);
    const [orders, setOrders] = useState([]);
    const [pageInfo, setPageInfo] = useState({ currentPage: 1, totalPage: 1 });

    const [query, setQuery] = useState({
        page: 1,
        size: 10,
        status: "", // 주문 상태 필터
        productName: "", // 상품명 검색
        loginId: "", // 주문자 아이디 검색
    });

    const fetchOrders = async () => {
        try {
        setLoading(true);
        const res = await getAdminOrderApi(query);
        setOrders(res.data.list || []);
        setPageInfo({
            currentPage: res.data.currentPage,
            totalPage: res.data.totalPage,
        });
        } catch (e) {
        toast.error("주문 내역을 불러오는데 실패했습니다.");
        } finally {
        setLoading(false);
        }
    };

    useEffect(() => {
        fetchOrders();
    }, [query.page, query.status]);

    const handleStatusChange = async (orderId, newStatus) => {

        const statusMap = {
            PAYMENT_DONE: "결제완료",
            DELIVERING: "배송중",
            COMPLETED: "배송완료",
            CANCELLED: "주문취소"
        };

        const statusName = statusMap[newStatus] || newStatus;

        if (!window.confirm(`주문 상태를 '${statusName}'(으)로 변경하시겠습니까?`)) return;

        try {
            await updateOrderStatusApi(orderId, newStatus);
            toast.success("상태가 변경되었습니다");
            fetchOrders(); // 목록 갱신
        } catch (e) {
            toast.error("상태 변경 실패");
        }
    };

    const onSearch = (e) => {
        e.preventDefault();
        setQuery(prev => ({...prev, page:1}));
        fetchOrders();
    };

    return (
    <div style={{ padding: 20 }}>
      <h2 style={{ marginBottom: 20, fontWeight: 700 }}>주문 관리 시스템</h2>

      {/* 검색 필터 영역 */}
      <form onSubmit={onSearch} style={{ display: "flex", gap: 10, marginBottom: 20, background: "#f8f9fa", padding: 15, borderRadius: 10 }}>
        <select 
          value={query.status} 
          onChange={(e) => setQuery(prev => ({ ...prev, status: e.target.value, page: 1 }))}
          style={{ padding: 8, borderRadius: 5, border: "1px solid #ddd" }}
        >
          <option value="">전체 상태</option>
          <option value="PAYMENT_DONE">결제완료</option>
          <option value="DELIVERING">배송중</option>
          <option value="COMPLETED">배송완료</option>
          <option value="CANCELLED">주문취소</option>
        </select>
        
        <input 
          placeholder="주문자 ID" 
          value={query.loginId}
          onChange={(e) => setQuery(prev => ({ ...prev, loginId: e.target.value }))}
          style={{ padding: 8, borderRadius: 5, border: "1px solid #ddd" }}
        />
        
        <input 
          placeholder="상품명 검색" 
          value={query.productName}
          onChange={(e) => setQuery(prev => ({ ...prev, productName: e.target.value }))}
          style={{ flex: 1, padding: 8, borderRadius: 5, border: "1px solid #ddd" }}
        />
        
        <button type="submit" style={{ padding: "8px 20px", background: "#333", color: "#fff", border: "none", borderRadius: 5, cursor: "pointer" }}>
          검색
        </button>
      </form>

      {/* 주문 목록 테이블 */}
      <div style={{ background: "#fff", border: "1px solid #eee", borderRadius: 10, overflow: "hidden" }}>
        <table style={{ width: "100%", borderCollapse: "collapse", textAlign: "left" }}>
          <thead style={{ background: "#f4f4f4" }}>
            <tr>
              <th style={{ padding: 12, borderBottom: "1px solid #ddd" }}>주문번호</th>
              <th style={{ padding: 12, borderBottom: "1px solid #ddd" }}>주문자(ID)</th>
              <th style={{ padding: 12, borderBottom: "1px solid #ddd" }}>상품정보</th>
              <th style={{ padding: 12, borderBottom: "1px solid #ddd" }}>결제금액</th>
              <th style={{ padding: 12, borderBottom: "1px solid #ddd" }}>상태관리</th>
              <th style={{ padding: 12, borderBottom: "1px solid #ddd" }}>주문일시</th>
            </tr>
          </thead>
          <tbody>
            {orders.length > 0 ? (
                orders.map((ord) => (
                <tr key={ord.id} style={{ borderBottom: "1px solid #eee" }}>
                    <td style={{ padding: 12 }}>{ord.id}</td>
                    <td style={{ padding: 12 }}>{ord.loginId}</td>
                    <td style={{ padding: 12 }}>
                    {ord.orderItems && ord.orderItems.length > 0 ? (
                        <>
                        <div style={{ fontWeight: 600 }}>{ord.orderItems[0].productName}</div>
                        {ord.orderItems.length > 1 && (
                            <div style={{ fontSize: 12, color: "#888" }}>외 {ord.orderItems.length - 1}건</div>
                        )}
                        </>
                    ) : (
                        <div style={{ color: "#999" }}>상품 정보 없음</div>
                    )}
                    </td>
                    <td style={{ padding: 12 }}>{ord.totalPrice?.toLocaleString()}원</td>
                    <td style={{ padding: 12 }}>
                    <select 
                        value={ord.status || ""} 
                        onChange={(e) => handleStatusChange(ord.id, e.target.value)}
                        style={{ 
                        padding: "5px 10px", 
                        borderRadius: 5, 
                        border: "1px solid #ccc",
                        background: ord.status === 'CANCELLED' ? '#fff0f0' : '#fff'
                        }}
                    >
                        <option value="PAYMENT_DONE">결제완료</option>
                        <option value="DELIVERING">배송중</option>
                        <option value="COMPLETED">배송완료</option>
                        <option value="CANCELLED">주문취소</option>
                    </select>
                    </td>
                    <td style={{ padding: 12, fontSize: 13, color: "#666" }}>{ord.createdAt}</td>
                </tr>
                ))
            ) : (
                <tr>
                <td colSpan="6" style={{ padding: 40, textAlign: "center", color: "#999" }}>
                    주문 내역이 없습니다.
                </td>
                </tr>
            )}
</tbody>
        </table>
      </div>

      {/* 페이지네이션 */}
      <div style={{ display: "flex", justifyContent: "center", alignItems: "center", gap: 15, marginTop: 20 }}>
        <button 
          onClick={() => setQuery(prev => ({ ...prev, page: prev.page - 1 }))}
          disabled={query.page === 1}
          style={{ padding: "5px 15px", borderRadius: 5, border: "1px solid #ddd", cursor: "pointer" }}
        >
          이전
        </button>
        <span>{pageInfo.currentPage} / {pageInfo.totalPage}</span>
        <button 
          onClick={() => setQuery(prev => ({ ...prev, page: prev.page + 1 }))}
          disabled={query.page === pageInfo.totalPage}
          style={{ padding: "5px 15px", borderRadius: 5, border: "1px solid #ddd", cursor: "pointer" }}
        >
          다음
        </button>
      </div>
    </div>
    );
}