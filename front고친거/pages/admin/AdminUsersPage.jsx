import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { getAdminUsersApi } from "@/api/adminUsersApi"

const PAGE_SIZE = 10;

export default function AdminUsersPage() {
  const [loading, setLoading] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);


// 1. 검색 및 페이지 상태 (백엔드 UserSearchDto와 매칭)
  const [query, setQuery] = useState({
    page: 1,
    size: PAGE_SIZE,
    startDate: "",
    endDate: "",
    keywordType: "all",
    keyword: "",
  });

  // 2. 서버에서 받은 페이징 데이터 상태
  const [pageData, setPageData] = useState({
    list: [],
    totalPage: 1,
    currentPage: 1,
  });

  // 3. 회원 목록 로드 함수
  const fetchUsers = async () => {
    try {
      setLoading(true);
      const res = await getAdminUsersApi(query);
      const data = res.data;

      // PageResponseDto 구조에 맞춰 데이터 세팅
      setPageData({
        list: data.list || [],
        totalPage: data.totalPage || 1,
        currentPage: data.currentPage || query.page,
      });
    } catch (e) {
      toast.error("회원 목록 조회 중 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

  // 페이지가 변경될 때마다 자동 호출
  useEffect(() => {
    fetchUsers();
  }, [query.page]);

  // 검색 버튼 클릭 시
  const onSearch = (e) => {
    e.preventDefault();
    setQuery((prev) => ({ ...prev, page: 1 }));
    fetchUsers();
  };
  
  return (
    <div style={{ padding: 24 }}>
      <h2 style={{ fontSize: 22, fontWeight: 700, marginBottom: 20 }}>회원 관리</h2>

      {/* [검색 섹션] */}
      <form onSubmit={onSearch} style={{ 
        display: "flex", gap: 10, marginBottom: 20, padding: 15, 
        background: "#f8f9fa", borderRadius: 10, border: "1px solid #eee" 
      }}>
        <input 
          type="date" 
          value={query.startDate}
          onChange={(e) => setQuery({...query, startDate: e.target.value})}
          style={{ padding: "8px 12px", borderRadius: 6, border: "1px solid #ddd" }}
        />
        <span style={{ alignSelf: "center" }}>~</span>
        <input 
          type="date" 
          value={query.endDate}
          onChange={(e) => setQuery({...query, endDate: e.target.value})}
          style={{ padding: "8px 12px", borderRadius: 6, border: "1px solid #ddd" }}
        />
        <select 
          value={query.keywordType}
          onChange={(e) => setQuery({...query, keywordType: e.target.value})}
          style={{ padding: "8px 12px", borderRadius: 6, border: "1px solid #ddd" }}
        >
          <option value="all">전체</option>
          <option value="loginId">아이디</option>
          <option value="userName">이름</option>
        </select>
        <input
          placeholder="검색어를 입력하세요"
          value={query.keyword}
          onChange={(e) => setQuery({...query, keyword: e.target.value})}
          style={{ flex: 1, padding: "8px 12px", borderRadius: 6, border: "1px solid #ddd" }}
        />
        <button type="submit" disabled={loading} style={{ 
          padding: "8px 24px", background: "#333", color: "#fff", 
          borderRadius: 6, border: "none", cursor: "pointer", fontWeight: 700 
        }}>
          검색
        </button>
      </form>

      {/* [메인 레이아웃] */}
      <div style={{ display: "grid", gridTemplateColumns: "1.4fr 1fr", gap: 20 }}>
        
        {/* 왼쪽: 목록 */}
        <div style={{ border: "1px solid #ddd", borderRadius: 12, overflow: "hidden", background: "#fff" }}>
          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead style={{ background: "#f1f3f5", borderBottom: "2px solid #dee2e6" }}>
              <tr>
                <th style={{ padding: 14 }}>아이디</th>
                <th style={{ padding: 14 }}>이름</th>
                <th style={{ padding: 14 }}>가입일</th>
              </tr>
            </thead>
            <tbody>
              {pageData.list.length > 0 ? (
                pageData.list.map((user) => (
                  <tr 
                    key={user.id} 
                    onClick={() => setSelectedUser(user)}
                    style={{ 
                      cursor: "pointer", 
                      borderBottom: "1px solid #eee",
                      background: selectedUser?.id === user.id ? "#eef6ff" : "transparent"
                    }}
                  >
                    <td style={{ padding: 14, textAlign: "center" }}>{user.loginId}</td>
                    <td style={{ padding: 14, textAlign: "center" }}>{user.userName}</td>
                    <td style={{ padding: 14, textAlign: "center" }}>
                      {user.createdAt ? user.createdAt.split("T")[0] : "-"}
                    </td>
                  </tr>
                ))
              ) : (
                <tr><td colSpan="3" style={{ padding: 30, textAlign: "center", color: "#999" }}>데이터가 없습니다.</td></tr>
              )}
            </tbody>
          </table>

          {/* 페이지네이션 */}
          <div style={{ display: "flex", justifyContent: "center", alignItems: "center", gap: 15, padding: 15, background: "#fdfdfd" }}>
            <button 
              onClick={() => setQuery(prev => ({ ...prev, page: prev.page - 1 }))}
              disabled={query.page <= 1}
              style={{ padding: "5px 10px", cursor: "pointer" }}
            >이전</button>
            <span style={{ fontWeight: 700 }}>{pageData.currentPage} / {pageData.totalPage}</span>
            <button 
              onClick={() => setQuery(prev => ({ ...prev, page: prev.page + 1 }))}
              disabled={query.page >= pageData.totalPage}
              style={{ padding: "5px 10px", cursor: "pointer" }}
            >다음</button>
          </div>
        </div>

        {/* 오른쪽: 상세 보기 */}
        <div style={{ border: "1px solid #ddd", borderRadius: 12, padding: 20, background: "#fff" }}>
          <h3 style={{ marginTop: 0, paddingBottom: 10, borderBottom: "1px solid #eee" }}>회원 상세 정보</h3>
          {selectedUser ? (
            <div style={{ lineHeight: "2.4", fontSize: 15 }}>
              <div style={{ display: "flex", borderBottom: "1px solid #f9f9f9" }}>
                <span style={{ width: 100, color: "#777" }}>회원번호</span>
                <span style={{ fontWeight: 600 }}>{selectedUser.id}</span>
              </div>
              <div style={{ display: "flex", borderBottom: "1px solid #f9f9f9" }}>
                <span style={{ width: 100, color: "#777" }}>아이디</span>
                <span style={{ fontWeight: 600 }}>{selectedUser.loginId}</span>
              </div>
              <div style={{ display: "flex", borderBottom: "1px solid #f9f9f9" }}>
                <span style={{ width: 100, color: "#777" }}>이름</span>
                <span style={{ fontWeight: 600 }}>{selectedUser.userName}</span>
              </div>
              <div style={{ display: "flex", borderBottom: "1px solid #f9f9f9" }}>
                <span style={{ width: 100, color: "#777" }}>이메일</span>
                <span style={{ fontWeight: 600 }}>{selectedUser.email}</span>
              </div>
              <div style={{ display: "flex", borderBottom: "1px solid #f9f9f9" }}>
                <span style={{ width: 100, color: "#777" }}>전화번호</span>
                <span style={{ fontWeight: 600 }}>{selectedUser.phone || "-"}</span>
              </div>
              <div style={{ display: "flex", borderBottom: "1px solid #f9f9f9" }}>
                <span style={{ width: 100, color: "#777" }}>가입일</span>
                <span style={{ fontWeight: 600 }}>{selectedUser.createdAt}</span>
              </div>
              {/* <div style={{ marginTop: 20, padding: 12, background: "#f1f3f5", borderRadius: 8, textAlign: "center" }}>
                현재 상태: <strong style={{ color: selectedUser.status === 'ACTIVE' ? 'blue' : 'red' }}>{selectedUser.status}</strong>
              </div> */}
            </div>
          ) : (
            <div style={{ padding: "50px 0", textAlign: "center", color: "#ccc" }}>
              목록에서 회원을 선택해주세요.
            </div>
          )}
        </div>
      </div>
    </div>
  );
}