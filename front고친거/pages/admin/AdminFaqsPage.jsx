// src/pages/admin/AdminFaqsPage.jsx
import { useEffect, useMemo, useState } from "react";
import useAdminFaqStore from "@/stores/adminFaqStore";

export default function AdminFaqsPage() {
  const { items, pageInfo, query, loading, setQuery, fetch, create, update, remove } =
    useAdminFaqStore();

  // 검색 입력(Submit 시 반영)
  const [keywordInput, setKeywordInput] = useState(query.keyword || "");

  // 등록 폼
  const [createForm, setCreateForm] = useState({ title: "", content: "" });

  // 수정(인라인)
  const [editingId, setEditingId] = useState(null);
  const [editingForm, setEditingForm] = useState({ title: "", content: "" });

  useEffect(() => {
    fetch();
  }, [fetch]);

  const onSearch = (e) => {
    e.preventDefault();
    const keyword = keywordInput.trim();
    setQuery({ keyword, page: 1 });
    fetch({ keyword, page: 1 });
  };

  const onReset = () => {
    setKeywordInput("");
    setQuery({ keyword: "", page: 1 });
    fetch({ keyword: "", page: 1 });
  };

  const onCreate = async (e) => {
    e.preventDefault();
    const title = createForm.title.trim();
    const content = createForm.content.trim();
    if (!title) return alert("제목을 입력해주십시오");
    if (!content) return alert("내용을 입력해주십시오");

    await create({ title, content });
    setCreateForm({ title: "", content: "" });
  };

  const startEdit = (faq) => {
    const id = faq?._faqId;
    setEditingId(id);
    setEditingForm({ title: faq?._title ?? "", content: faq?._content ?? "" });
  };

  const cancelEdit = () => {
    setEditingId(null);
    setEditingForm({ title: "", content: "" });
  };

  const submitEdit = async () => {
    const title = editingForm.title.trim();
    const content = editingForm.content.trim();
    if (!title) return alert("제목을 입력해주십시오");
    if (!content) return alert("내용을 입력해주십시오");

    await update(editingId, { title, content });
    cancelEdit();
  };

  const onDelete = async (faq) => {
    const id = faq?._faqId;
    const ok = window.confirm("정말 삭제하겠습니까?");
    if (!ok) return;
    await remove(id);
  };

  const canPrev = pageInfo.page > 1;
  const canNext = pageInfo.page < pageInfo.totalPages;

  const pageLabel = useMemo(() => `${pageInfo.page} / ${pageInfo.totalPages}`, [pageInfo.page, pageInfo.totalPages]);

  return (
    <div className="p-4">
      <h1 className="text-xl font-semibold mb-4">관리자 · FAQ 관리</h1>

      {/* 검색 */}
      <form onSubmit={onSearch} className="border rounded-lg p-3 mb-4 flex flex-col gap-2">
        <div className="flex gap-2 items-center">
          <label className="w-24 text-sm">검색</label>
          <input
            className="border rounded px-2 py-1 flex-1"
            value={keywordInput}
            onChange={(e) => setKeywordInput(e.target.value)}
            placeholder="키워드"
          />
        </div>
        <div className="flex justify-end gap-2">
          <button type="button" className="border rounded px-3 py-1" onClick={onReset}>
            초기화
          </button>
          <button type="submit" className="border rounded px-3 py-1">
            검색
          </button>
        </div>
      </form>

      {/* 등록 */}
      <form onSubmit={onCreate} className="border rounded-lg p-3 mb-4 flex flex-col gap-2">
        <div className="text-sm font-semibold">FAQ 등록</div>

        <div className="flex gap-2 items-center">
          <label className="w-24 text-sm">제목</label>
          <input
            className="border rounded px-2 py-1 flex-1"
            value={createForm.title}
            onChange={(e) => setCreateForm((p) => ({ ...p, title: e.target.value }))}
          />
        </div>

        <div className="flex gap-2 items-start">
          <label className="w-24 text-sm pt-1">내용</label>
          <textarea
            className="border rounded px-2 py-1 flex-1 min-h-[90px]"
            value={createForm.content}
            onChange={(e) => setCreateForm((p) => ({ ...p, content: e.target.value }))}
          />
        </div>

        <div className="flex justify-end">
          <button className="border rounded px-3 py-1" type="submit" disabled={loading}>
            등록
          </button>
        </div>
      </form>

      {/* 목록 */}
      <div className="border rounded-lg overflow-hidden">
        <div className="px-3 py-2 border-b flex items-center justify-between">
          <div className="text-sm">
            페이지 <b>{pageLabel}</b>
          </div>
          <div className="flex gap-2">
            <button
              className="border rounded px-3 py-1 text-sm"
              type="button"
              onClick={() => {
                const next = Math.max(1, pageInfo.page - 1);
                setQuery({ page: next });
                fetch({ page: next });
              }}
              disabled={!canPrev || loading}
            >
              이전
            </button>
            <button
              className="border rounded px-3 py-1 text-sm"
              type="button"
              onClick={() => {
                const next = pageInfo.page + 1;
                setQuery({ page: next });
                fetch({ page: next });
              }}
              disabled={!canNext || loading}
            >
              다음
            </button>
            <button
              className="border rounded px-3 py-1 text-sm"
              type="button"
              onClick={() => fetch()}
              disabled={loading}
            >
              새로고침
            </button>
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b">
                <th className="p-2 text-left">ID</th>
                <th className="p-2 text-left">제목</th>
                <th className="p-2 text-left">내용</th>
                <th className="p-2 text-left">작업</th>
              </tr>
            </thead>
            <tbody>
              {loading && (
                <tr>
                  <td colSpan={4} className="p-4 text-center">
                    불러오는 중...
                  </td>
                </tr>
              )}

              {!loading && items.length === 0 && (
                <tr>
                  <td colSpan={4} className="p-4 text-center">
                    FAQ가 없습니다.
                  </td>
                </tr>
              )}

              {!loading &&
                items.map((faq) => {
                  const id = faq._faqId;
                  const isEditing = editingId === id;

                  return (
                    <tr key={id} className="border-b align-top">
                      <td className="p-2">{id}</td>

                      <td className="p-2 w-[220px]">
                        {isEditing ? (
                          <input
                            className="border rounded px-2 py-1 w-full"
                            value={editingForm.title}
                            onChange={(e) =>
                              setEditingForm((s) => ({ ...s, title: e.target.value }))
                            }
                          />
                        ) : (
                          faq._title
                        )}
                      </td>

                      <td className="p-2">
                        {isEditing ? (
                          <textarea
                            className="border rounded px-2 py-1 w-full min-h-[90px]"
                            value={editingForm.content}
                            onChange={(e) =>
                              setEditingForm((s) => ({ ...s, content: e.target.value }))
                            }
                          />
                        ) : (
                          <div className="whitespace-pre-wrap">{faq._content}</div>
                        )}
                      </td>

                      <td className="p-2 w-[140px]">
                        {isEditing ? (
                          <div className="flex flex-col gap-2">
                            <button
                              type="button"
                              className="border rounded px-2 py-1"
                              onClick={submitEdit}
                            >
                              저장
                            </button>
                            <button
                              type="button"
                              className="border rounded px-2 py-1"
                              onClick={cancelEdit}
                            >
                              취소
                            </button>
                          </div>
                        ) : (
                          <div className="flex flex-col gap-2">
                            <button
                              type="button"
                              className="border rounded px-2 py-1"
                              onClick={() => startEdit(faq)}
                            >
                              수정
                            </button>
                            <button
                              type="button"
                              className="border rounded px-2 py-1"
                              onClick={() => onDelete(faq)}
                            >
                              삭제
                            </button>
                          </div>
                        )}
                      </td>
                    </tr>
                  );
                })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}