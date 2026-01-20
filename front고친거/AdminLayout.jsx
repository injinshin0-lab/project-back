import { Outlet, useNavigate, useLocation } from "react-router-dom";

export default function AdminLayout() {
  const navigate = useNavigate();
  const location = useLocation();

  // ✅ 관리자 메뉴 설정
  const tabs = [
    { name: "대시보드", path: "/admin" },
    { name: "상품관리", path: "/admin/products" },
    { name: "카테고리", path: "/admin/categories" },
    { name: "회원관리", path: "/admin/users" },
    { name: "주문관리", path: "/admin/order" },
    { name: "문의관리(Q&A)", path: "/admin/qna" },
    { name: "FAQ관리", path: "/admin/faqs" },
    { name: "알림", path: "/admin/alarm" },
  ];

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 py-8">
        {/* 헤더 영역 */}
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-2xl font-bold text-gray-900">관리자 시스템</h1>
          <button 
            onClick={() => navigate("/bogam")}
            className="text-sm text-gray-500 hover:text-black border px-3 py-1 rounded"
          >
            사용자 페이지로 이동
          </button>
        </div>

        {/* ✅ 공통 탭 메뉴 */}
        <div className="flex flex-wrap gap-2 mb-6 border-b border-gray-200 pb-4">
          {tabs.map((tab) => {
            const isActive = location.pathname === tab.path;
            return (
              <button
                key={tab.path}
                onClick={() => navigate(tab.path)}
                className={`px-5 py-2.5 rounded-xl text-sm font-semibold transition-all ${
                  isActive 
                    ? "bg-black text-white shadow-lg" 
                    : "bg-white text-gray-500 hover:bg-gray-100 border border-gray-200"
                }`}
              >
                {tab.name}
              </button>
            );
          })}
        </div>

        {/* ✅ 실제 페이지 콘텐츠가 렌더링되는 곳 */}
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
          <Outlet />
        </div>
      </div>
    </div>
  );
}