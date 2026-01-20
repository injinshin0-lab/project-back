// src/pages/admin/AdminQnaPage.jsx
import { useEffect, useMemo, useState } from "react";
import toast from "react-hot-toast";

import { getAdminInquiriesApi, replyAdminInquiryApi } from "@/api/inquiryApi";

const PAGE_SIZE = 10;

export default function AdminQnaPage() {
  const [loading, setLoading] = useState(false);

  const [query, setQuery] = useState({
    page: 1,
    size: PAGE_SIZE,
    keyword: "",
  });

  const [pageData, setPageData] = useState({
    list: [],
    totalPage: 1,
    currentPage: 1,
  });

  const [selected, setSelected] = useState(null);

  const [replyForm, setReplyForm] = useState({
    content: "",
  });

  const isSelected = useMemo(() => !!selected?.id, [selected]);

  const fetchList = async () => {
    try {
      setLoading(true);
      const res = await getAdminInquiriesApi(query);
      const data = res?.data || {};

      setPageData({
        list: data.list || data.items || [],
        totalPage: data.totalPage || data.totalPages || 1,
        currentPage: data.currentPage || query.page,
      });
    } catch (e) {
      toast.error("1:1 문의 목록 조회 실패");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchList();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [query.page]);

  const onSearch = (e) => {
    e.preventDefault();
    setQuery((prev) => ({ ...prev, page: 1 })); // page 1로 리셋
    fetchList();
  };

  const onSelect = (row) => {
    setSelected(row);
    // 백엔드가 list에 replyContent 같은 걸 내려주면 거기서 세팅
    const existing = row?.answer?.content || "";
    setReplyForm({ content: existing || "" });
  };

  const onSubmitReply = async () => {
    if (!selected?.id) return toast.error("문의 선택이 필요합니다.");

    const content = replyForm.content.trim();
    if (!content) return toast.error("답변 내용을 입력하세요.");

    try {
      setLoading(true);

      // ✅ payload 키는 백엔드 DTO에 맞춰야 함
      // 우선 가장 흔한 content로 보내고,
      // 백엔드가 replyContent로 받으면 여기만 바꾸면 됨.
      await replyAdminInquiryApi(selected.id, { content });

      toast.success("답변이 등록되었습니다.");

      setSelected((prev) => ({
        ...prev,
        answer: { 
          content: content 
        }, 
        questionStatus: "ANSWERED"
      }));

      // setSelected(prev => ({
      //   ...prev,
      //   answer: { content } // 답변 객체를 즉시 주입
      // }));

      await fetchList();
    } catch (e) {
      toast.error("답변 등록/수정 실패");
    } finally {
      setLoading(false);
    }
  };

  const goPage = (p) => {
    setQuery((prev) => ({ ...prev, page: p }));
  };

  return (
    <div style={{ padding: 16 }}>
      <h2 style={{ fontSize: 20, fontWeight: 700, marginBottom: 12 }}>
        1:1 문의 관리
      </h2>

      {/* 검색 */}
      <form onSubmit={onSearch} style={{ display: "flex", gap: 8, marginBottom: 12 }}>
        <input
          value={query.keyword}
          onChange={(e) =>
            setQuery((prev) => ({ ...prev, keyword: e.target.value }))
          }
          placeholder="키워드 검색"
          style={{ flex: 1, padding: 10, border: "1px solid #ccc", borderRadius: 8 }}
        />
        <button
          type="submit"
          disabled={loading}
          style={{
            padding: "10px 14px",
            borderRadius: 8,
            border: "1px solid #333",
            background: "#fff",
            cursor: "pointer",
          }}
        >
          검색
        </button>
      </form>

      <div style={{ display: "grid", gridTemplateColumns: "1.2fr 1fr", gap: 12 }}>
        {/* 리스트 */}
        <div style={{ border: "1px solid #ddd", borderRadius: 10, overflow: "hidden" }}>
          <div style={{ padding: 10, fontWeight: 700, background: "#f7f7f7" }}>
            문의 목록
          </div>

          <div style={{ maxHeight: 520, overflow: "auto" }}>
            {pageData.list.length === 0 ? (
              <div style={{ padding: 14, color: "#666" }}>데이터가 없습니다.</div>
            ) : (
              pageData.list.map((row) => {
                const active = selected?.id === row.id;
                return (
                  <div
                    key={row.id}
                    onClick={() => onSelect(row)}
                    style={{
                      padding: 12,
                      borderTop: "1px solid #eee",
                      cursor: "pointer",
                      background: active ? "#eef6ff" : "#fff",
                    }}
                  >
                    <div style={{ fontWeight: 700 }}>
                      {row.title || row.subject || "(제목 없음)"}
                    </div>
                    <div style={{ fontSize: 12, color: "#666", marginTop: 4 }}>
                      작성자: {row.userName || row.loginId || row.userId || "-"} · 상태:{" "}
                      {row.status || row.questionStatus || "-"}
                    </div>
                  </div>
                );
              })
            )}
          </div>

          

          {/* 페이지네이션 */}
          <div style={{ display: "flex", gap: 6, padding: 10, borderTop: "1px solid #eee" }}>
            <button
              onClick={() => goPage(Math.max(1, pageData.currentPage - 1))}
              disabled={loading || pageData.currentPage <= 1}
              style={{ padding: "6px 10px", borderRadius: 8, border: "1px solid #ccc" }}
            >
              이전
            </button>
            <div style={{ padding: "6px 10px" }}>
              {pageData.currentPage} / {pageData.totalPage}
            </div>
            <button
              onClick={() => goPage(Math.min(pageData.totalPage, pageData.currentPage + 1))}
              disabled={loading || pageData.currentPage >= pageData.totalPage}
              style={{ padding: "6px 10px", borderRadius: 8, border: "1px solid #ccc" }}
            >
              다음
            </button>
          </div>
        </div>

        {/* 상세/답변 박스 */}
        <div style={{ border: "1px solid #ddd", borderRadius: 10, overflow: "hidden", background: "#fff" }}>
          <div style={{ padding: 10, fontWeight: 700, background: "#f7f7f7", borderBottom: "1px solid #ddd" }}>
            문의 상세 정보
          </div>

          {!isSelected ? (
            <div style={{ padding: 20, color: "#999", textAlign: "center" }}>
              왼쪽 목록에서 문의를 선택하면 상세 내용이 표시됩니다.
            </div>
          ) : (
            <div style={{ padding: 16 }}>

              <div style={{ 
                display: "flex", alignItems: "center", gap: 12, 
                padding: 12, background: "#f8f9fa", borderRadius: 10, 
                marginBottom: 20, border: "1px solid #eee" 
              }}>
                {selected.productImageUrl || selected.imageUrl ? (
                  <img 
                    src={encodeURI(`http://localhost:8080/uploads/${selected.productImageUrl.replace(/^\//, '')}`)}
                    alt="상품이미지"
                    style={{ width: 50, height: 50, borderRadius: 6, objectFit: "cover" }}
                  />
                ) : (
                  <div style={{ width: 50, height: 50, background: "#eee", borderRadius: 6, display: "flex", alignItems: "center", justifyContent: "center", fontSize: 10, color: "#999" }}>No Img</div>
                )}
                <div>
                  <div style={{ fontSize: 11, color: "#888" }}>문의 관련 상품</div>
                  <div style={{ fontWeight: 700, fontSize: 14 }}>
                    {selected.productName || "일반 문의"}
                  </div>
                </div>
              </div>
              {/* 문의 본문 섹션 */}
              <div style={{ marginBottom: 20 }}>
                <div style={{ fontSize: 13, color: "#888", marginBottom: 4 }}>Q. 고객 문의 내용</div>
                <div style={{ fontWeight: 800, fontSize: 16, marginBottom: 8 }}>
                  {selected.title}
                </div>
                <div style={{ 
                  whiteSpace: "pre-wrap", 
                  padding: 14, 
                  background: "#f9f9f9", 
                  borderRadius: 8, 
                  border: "1px solid #eee",
                  lineHeight: "1.5"
                }}>
                  {selected.content}
                </div>
              </div>

              {/* 문의 첨부 이미지 */}
                <div style={{ marginBottom: 20 }}>
                  <div style={{ fontSize: 13, color: "#888", marginBottom: 8 }}>📎 첨부된 사진</div>
                  
                  {selected.questionImages && selected.questionImages.length > 0 ? (
                    <div style={{ display: "flex", gap: 10, flexWrap: "wrap" }}>
                      {selected.questionImages.map((img, idx) => (
                        <div key={idx} style={{ position: "relative" }}>
                          <img
                            // 경로 규칙에 맞춰 수정 (uploads 폴더 기준)
                            src={img.imageUrl.startsWith('http') 
                              ? img.imageUrl 
                              : `http://localhost:8080/uploads/${img.imageUrl.replace(/^\//, '')}`
                            }
                            alt={`문의사진-${idx}`}
                            style={{
                              width: "100px", height: "100px", objectFit: "cover",
                              borderRadius: 8, border: "1px solid #ddd", cursor: "zoom-in"
                            }}
                            onClick={() => window.open(img.imageUrl.startsWith('http') ? img.imageUrl : `http://localhost:8080/uploads/${img.imageUrl.replace(/^\//, '')}`, "_blank")}
                          />
                        </div>
                      ))}
                    </div>
                  ) : (
                    <div style={{ 
                      padding: "15px", background: "#fdfdfd", border: "1px dashed #ccc", 
                      borderRadius: 8, color: "#999", fontSize: 13, textAlign: "center" 
                    }}>
                      첨부된 사진이 없습니다.
                    </div>
                  )}
                </div>

              <hr style={{ border: "none", borderTop: "1px solid #eee", margin: "20px 0" }} />
              {/* 답변 섹션 */}
              <div>
                <div style={{ fontSize: 13, color: "#888", marginBottom: 8 }}>A. 관리자 답변</div>
                
                {/* 이미 답변이 있는 경우: 등록된 내용 표시 (수정 불가 모드) */}
                {selected?.answer?.content ? (
                  <div style={{ animation: "fadeIn 0.3s ease" }}>
                    <div style={{ 
                      whiteSpace: "pre-wrap", 
                      padding: 14, 
                      background: "#eef6ff", 
                      borderRadius: 8, 
                      border: "1px solid #d1e3f8",
                      color: "#333",
                      fontWeight: 500
                    }}>
                      {selected.answer.content}
                    </div>
                    <div style={{ marginTop: 10, textAlign: "right", color: "#2b6cb0", fontSize: 12, fontWeight: 700 }}>
                      ✓ 답변 완료 (수정하려면 데이터베이스 관리가 필요합니다)
                    </div>
                  </div>
                ) : (
                  /* 답변이 없는 경우: 입력창 표시 */
                  <>
                    <textarea
                      value={replyForm.content}
                      onChange={(e) => setReplyForm({ content: e.target.value })}
                      placeholder="답변 내용을 입력해 주세요. 등록 후에는 수정이 제한될 수 있습니다."
                      rows={6}
                      style={{
                        width: "100%",
                        padding: 12,
                        border: "1px solid #ccc",
                        borderRadius: 8,
                        resize: "none",
                        fontSize: 14
                      }}
                    />
                    <button
                      onClick={onSubmitReply}
                      disabled={loading || !replyForm.content.trim()}
                      style={{
                        marginTop: 10,
                        width: "100%",
                        padding: "12px",
                        borderRadius: 8,
                        border: "none",
                        background: loading ? "#ccc" : "#333",
                        color: "#fff",
                        cursor: loading ? "default" : "pointer",
                        fontWeight: 700,
                      }}
                    >
                      {loading ? "등록 중..." : "답변 확정 등록"}
                    </button>
                  </>
                )}
              </div>
            </div>
          )}
        </div>  
      </div>
    </div>
  );
}