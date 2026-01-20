// src/pages/admin/AdminProductsPage.jsx
import { useEffect, useMemo, useState } from "react";
import useAdminProductStore from "@/stores/adminProductStore";
import { getCategoriesApi } from "@/api/categoryApi"; // ✅ 이미 프로젝트에 있음(없으면 경로만 맞춰줘)


// 1. 트리 변환 헬퍼 함수
function buildCategoryTree(nodes) {
  const map = {};
  const tree = [];
  nodes.forEach((node) => { map[node.id] = { ...node, children: [] }; });
  nodes.forEach((node) => {
    if (node.parentId && map[node.parentId]) {
      map[node.parentId].children.push(map[node.id]);
    } else {
      tree.push(map[node.id]);
    }
  });
  return tree;
}

// 2. 트리 아이템 컴포넌트 (파일 상단 고정)
function CategoryTreeItem({ node, selectedIds, onChange }) {
  const [isOpen, setIsOpen] = useState(false); // 🔥 접힘 상태 추가
  const isChecked = selectedIds.includes(Number(node.id));
  const hasChildren = node.children && node.children.length > 0;

  return (
    <div className="ml-3 my-0.5">
      <div className="flex items-center gap-1">
        {/* 🔥 화살표 아이콘: 자식이 있을 때만 노출 */}
        <button
          type="button"
          onClick={() => setIsOpen(!isOpen)}
          className={`w-5 h-5 flex items-center justify-center transition-transform ${!hasChildren ? 'invisible' : ''} ${isOpen ? 'rotate-90' : ''}`}
        >
          <span className="text-[10px] text-gray-400">▶</span>
        </button>

        <label className={`flex items-center gap-2 cursor-pointer p-1 rounded-md transition-colors flex-1 ${isChecked ? 'bg-blue-50 text-blue-700' : 'hover:bg-gray-100'}`}>
          <input
            type="checkbox"
            checked={isChecked}
            onChange={(e) => onChange(node.id, e.target.checked)}
            className="w-4 h-4 accent-blue-600"
          />
          <span className={`text-sm ${isChecked ? 'font-bold' : 'text-gray-700'}`}>
            {node.name}
          </span>
        </label>
      </div>

      {/* 🔥 isOpen이 true일 때만 자식 노드 렌더링 */}
      {hasChildren && isOpen && (
        <div className="border-l border-gray-200 ml-2">
          {node.children.map((child) => (
            <CategoryTreeItem key={child.id} node={child} selectedIds={selectedIds} onChange={onChange} />
          ))}
        </div>
      )}
    </div>
  );
}

function toNumber(v, fallback = NaN) {
  const n = Number(v);
  return Number.isFinite(n) ? n : fallback;
}

function pickId(p) {
  return p?.productId ?? p?.id;
}

function pickName(p) {
  return p?.productName ?? p?.name ?? "";
}

function pickCategoryName(p) {
  // 1. 우선 배열(categories 또는 categoryList)이 있는지 확인
  const list = p?.categories || p?.categoryList || [];
  
  if (Array.isArray(list) && list.length > 0) {
    // 모든 카테고리 이름을 쉼표(,)로 연결 (예: 상의, 반팔)
    return list.map(c => c.categoryName).join(", ");
  }

  return "미지정";
}

export default function AdminProductsPage() {
  const { items, pageInfo, query, loading, setQuery, fetch, create, update, remove } = useAdminProductStore();

  
  const [fileInputKey, setFileInputKey] = useState(Date.now());

  // 크게 볼 이미지
  const [previewImage, setPreviewImage] = useState(null); // 크게 볼 이미지 URL 상태

  // ✅ 카테고리 select
  const [categories, setCategories] = useState([]);
  const [catLoading, setCatLoading] = useState(false);

  // ✅ 검색/필터 입력값(즉시 store에 박지 않고 submit 시 반영)
  const [keywordInput, setKeywordInput] = useState(query.keyword || "");
  const [categoryInput, setCategoryInput] = useState(query.categoryId || "");

  // 검색용 카테고리 체크박스 토글 함수
  const handleSearchCategoryToggle = (id, checked) => {
    setCategoryInput((prev) =>
      checked 
        ? [...prev, Number(id)] 
        : prev.filter((cid) => cid !== Number(id))
    );
  };

  // ✅ 등록 폼
  const [createForm, setCreateForm] = useState({
    productName: "", price: "", originName: "", content: "", categoryId: [], imageFile: null,
  });

  // ✅ 수정(Inline)
  const [editingId, setEditingId] = useState(null);
  const [editingForm, setEditingForm] = useState({
    productName: "", price: "", originName: "", content: "", categoryId: [], imageFile: null,
  }); 

  useEffect(() => {
    fetch();
  }, [fetch]);

  useEffect(() => {
    (async () => {
      try {
        setCatLoading(true);
        const res = await getCategoriesApi();
        const data = res?.data?.data ?? res?.data ?? [];
        setCategories(Array.isArray(data) ? data : []);
      } catch (e) {
        // 카테고리 로딩 실패해도 상품 관리는 가능하게
        setCategories([]);
      } finally {
        setCatLoading(false);
      }
    })();
  }, []);

  const categoryOptions = useMemo(() => {
    return (categories || []).map((c) => ({
      id: c?.categoryId ?? c?.id,
      name: c?.categoryName ?? c?.name,
      parentId: c?.parentId ?? null,
    }));
  }, [categories]);

  // 트리 구조 데이터 생성
  const categoryTree = useMemo(() => buildCategoryTree(categoryOptions), [categoryOptions]);

  // 체크박스 토글 함수
  const handleCategoryToggle = (id, checked, isEdit = false) => {
    const setter = isEdit ? setEditingForm : setCreateForm;
    setter((prev) => {
      const currentIds = Array.isArray(prev.categoryId) ? prev.categoryId: [];
      const nextIds = checked
      ? [...currentIds, Number(id)]
      : currentIds.filter((cid) => cid !== Number(id));
      return {...prev, categoryId: nextIds};
    });
  };

  const onSearch = (e) => {
    e.preventDefault();
    const keyword = keywordInput.trim();

    setQuery({ keyword, categoryIds: categoryInput, page: 1 });
    fetch({ keyword,  categoryIds: categoryInput, page: 1});
  };

  const onReset = () => {
    setKeywordInput("");
    setCategoryInput([]);
    setQuery({ keyword: "", categoryIds: [], page: 1 });
    fetch({ keyword: "", categoryIds: [], page: 1 });
  };

  const onCreate = async (e) => {
    console.log("현재 선택된 카테고리들:", createForm.categoryId);
    e.preventDefault();

    const productName = createForm.productName.trim();
    const price = toNumber(createForm.price);
    if (!productName) return alert("상품명을 입력해주십시오");
    if (!Number.isFinite(price)) return alert("가격을 숫자로 입력해주십시오");
    if (!createForm.categoryId) return alert("카테고리를 입력해주십시오");

    await create({
      productName,
      price,
      originName: createForm.originName.trim(),
      content: createForm.content.trim(),
      categoryIds: createForm.categoryId,
      imageFile: createForm.imageFile, // ✅ File
    });
    // 폼 데이터 리셋
    setCreateForm({
      productName: "",
      price: "",
      originName: "",
      content: "",
      categoryId: [],
      imageFile: null,
    });

    //등록 성공 후 파일 입력창 '선택된 파일 없음'으로 리셋
    setFileInputKey(Date.now());
  };

  const startEdit = (p) => {
    const id = pickId(p);
    setEditingId(id);

    const currentCategoryIds = Array.isArray(p.categories) 
    ? p.categories.map(c => c.categoryId) 
    : [];

    setEditingForm({
      productName: pickName(p),
      price: String(p?.price ?? ""),
      originName: String(p?.originName ?? ""),
      content: String(p?.content ?? ""),
      categoryId: currentCategoryIds,
      imageFile: null,
    });
  };

  const cancelEdit = () => {
    setEditingId(null);
    setEditingForm({
      productName: "",
      price: "",
      originName: "",
      content: "",
      categoryId: [],
      imageFile: null,
    });
  };

  const submitEdit = async () => {
    const productName = editingForm.productName.trim();
    const price = toNumber(editingForm.price);
    if (!productName) return alert("상품명을 입력해주십시오");
    if (!Number.isFinite(price)) return alert("가격을 숫자로 입력해주십시오");
    if (!editingForm.categoryId) return alert("카테고리를 입력해주십시오");

    await update(editingId, {
      productName,
      price,
      originName: editingForm.originName.trim(),
      content: editingForm.content.trim(),
      categoryIds: editingForm.categoryId,
      imageFile: editingForm.imageFile, // ✅ 있으면 교체 업로드
    });

    cancelEdit();
  };

  const onDelete = async (p) => {
    const id = pickId(p);
    const ok = window.confirm("정말 삭제하겠습니까?");
    if (!ok) return;
    await remove(id);
  };

  const canPrev = pageInfo.page > 1;
  const canNext = pageInfo.page < pageInfo.totalPages;

  return (
    <div className="p-4">
      <h1 className="text-xl font-semibold mb-4">관리자 · 상품 관리</h1>

      

      {/* 등록 */}
      <form onSubmit={onCreate} className="border rounded-lg p-3 mb-4 flex flex-col gap-2">
        <div className="text-sm font-semibold">새 상품 등록</div>

        <div className="flex gap-2 items-center">
          <label className="w-24 text-sm">상품명</label>
          <input
            className="border rounded px-2 py-1 flex-1"
            value={createForm.productName}
            onChange={(e) => setCreateForm((p) => ({ ...p, productName: e.target.value }))}
          />
        </div>

        <div className="flex gap-2 items-center">
          <label className="w-24 text-sm">가격</label>
          <input
            className="border rounded px-2 py-1 flex-1"
            value={createForm.price}
            onChange={(e) => setCreateForm((p) => ({ ...p, price: e.target.value }))}
            inputMode="numeric"
          />
        </div>

        <div className="flex gap-2 items-center">
          <label className="w-24 text-sm">원산지</label>
          <input
            className="border rounded px-2 py-1 flex-1"
            value={createForm.originName}
            onChange={(e) => setCreateForm((p) => ({ ...p, originName: e.target.value }))}
          />
        </div>

        <div className="flex gap-2 items-start">
          <label className="w-24 text-sm pt-1">설명</label>
          <textarea
            className="border rounded px-2 py-1 flex-1 min-h-[80px]"
            value={createForm.content}
            onChange={(e) => setCreateForm((p) => ({ ...p, content: e.target.value }))}
          />
        </div>
        

        <div className="flex gap-2 items-start">
        <label className="w-24 text-sm pt-1">카테고리</label>
        <div className="border rounded px-2 py-2 flex-1 max-h-60 overflow-y-auto bg-gray-50 shadow-inner">
          {catLoading ? (
            <div className="text-xs text-gray-400 p-2">로딩 중...</div>
          ) : (
            categoryTree.map((node) => (
              <CategoryTreeItem
                key={node.id}
                node={node}              
                selectedIds={createForm.categoryId}
                onChange={(id, checked) => handleCategoryToggle(id, checked, false)}
              />
            ))
          )}
        </div>
      </div>

        <div className="flex gap-2 items-center">
          <label className="w-24 text-sm">이미지</label>
          <input
            key={fileInputKey}
            className="border rounded px-2 py-1 flex-1"
            type="file"
            accept="image/*"
            onChange={(e) =>
              setCreateForm((p) => ({ ...p, imageFile: e.target.files?.[0] ?? null }))
            }
          />
        </div>

        <div className="flex justify-end">
          <button className="border rounded px-3 py-1" type="submit" disabled={loading}>
            등록
          </button>
        </div>
      </form>

      {/* 검색/필터 */}
      {/* <form onSubmit={onSearch} className="border rounded-lg p-3 mb-4 flex flex-col gap-2">
        <div className="flex gap-2 items-start">

          <div className="flex gap-2 items-center">
            <label className="w-24 text-sm font-medium text-gray-600">검색어</label>
            <input
              className="border rounded px-3 py-2 flex-1 bg-white focus:ring-2 focus:ring-blue-400 outline-none transition-all"
              value={keywordInput}
              onChange={(e) => setKeywordInput(e.target.value)}
              placeholder="상품명, 설명 또는 원산지 입력"
            />
          </div>
          <label className="w-24 text-sm pt-1">카테고리 필터</label>
          <div className="border rounded px-2 py-2 flex-1 max-h-40 overflow-y-auto bg-white shadow-sm">
            {catLoading ? (
              <div className="text-xs text-gray-400 p-2">로딩 중...</div>
            ) : (
              categoryTree.map((node) => (
                <CategoryTreeItem
                  key={node.id}
                  node={node}
                  selectedIds={categoryInput}
                  onChange={handleSearchCategoryToggle}
                />
              ))
            )}
          </div>
        </div>

        <div className="flex justify-end gap-2">
          <button className="border rounded px-3 py-1" type="button" onClick={onReset}>
            초기화
          </button>
          <button className="border rounded px-3 py-1" type="submit">
            검색
          </button>
        </div>
      </form> */}
      <form onSubmit={onSearch} className="border rounded-lg p-4 mb-4 flex flex-col gap-4 bg-white shadow-sm">
        {/* 첫 번째 행: 검색어 (길게 배치) */}
        <div className="flex gap-4 items-center">
          <label className="w-24 text-sm font-bold text-gray-600">검색어</label>
          <input
            className="border rounded-md px-4 py-2 flex-1 bg-gray-50 focus:ring-2 focus:ring-blue-400 focus:bg-white outline-none transition-all"
            value={keywordInput}
            onChange={(e) => setKeywordInput(e.target.value)}
            placeholder="상품명, 설명 또는 원산지 입력"
          />
        </div>

        {/* 두 번째 행: 카테고리 필터 (아래로 내림) */}
        <div className="flex gap-4 items-start">
          <label className="w-24 text-sm font-bold text-gray-600 pt-2">카테고리 필터</label>
          <div className="border rounded-md px-3 py-3 flex-1 max-h-48 overflow-y-auto bg-gray-50 shadow-inner">
            {catLoading ? (
              <div className="text-xs text-gray-400 p-2 text-center">카테고리를 불러오는 중...</div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-x-4"> {/* 카테고리가 많을 경우 대비해 그리드 적용 가능 */}
                {categoryTree.map((node) => (
                  <CategoryTreeItem
                    key={node.id}
                    node={node}
                    selectedIds={categoryInput}
                    onChange={handleSearchCategoryToggle}
                  />
                ))}
              </div>
            )}
          </div>
        </div>

        {/* 세 번째 행: 버튼 제어 */}
        <div className="flex justify-end gap-3 border-t pt-3">
          <button 
            className="px-4 py-2 text-sm font-medium text-gray-600 bg-gray-100 rounded-md hover:bg-gray-200 transition-colors" 
            type="button" 
            onClick={onReset}
          >
            초기화
          </button>
          <button 
            className="px-6 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 shadow-md transition-colors" 
            type="submit"
          >
            상품 검색
          </button>
        </div>
      </form>
      

      {/* 목록 */}
      <div className="border rounded-lg overflow-hidden">
        <div className="px-3 py-2 border-b flex items-center justify-between">
          <div className="text-sm">
            페이지 <b>{pageInfo.page}</b> / <b>{pageInfo.totalPages}</b>
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
                <th className="p-2 text-left">상품명</th>
                <th className="p-2 text-left">가격</th>
                <th className="p-2 text-left">카테고리</th>
                <th className="p-2 text-left w-1/4">설명</th>
                <th className="p-2 text-left">원산지</th>
                <th className="p-2 text-left">이미지</th>
                <th className="p-2 text-left">작업</th>
              </tr>
            </thead>

            <tbody>
              {loading && (
                <tr>
                  <td colSpan={7} className="p-4 text-center">
                    불러오는 중...
                  </td>
                </tr>
              )}

              {!loading && items.length === 0 && (
                <tr>
                  <td colSpan={7} className="p-4 text-center">
                    상품이 없습니다.
                  </td>
                </tr>
              )}

              {!loading &&
                items.map((p) => {
                  const id = pickId(p);
                  const isEditing = editingId === id;

                  return (
                    <tr key={id} className="border-b align-top">
                      <td className="p-2">{id}</td>

                      <td className="p-2">
                        {isEditing ? (
                          <input
                            className="border rounded px-2 py-1 w-full"
                            value={editingForm.productName}
                            onChange={(e) =>
                              setEditingForm((s) => ({ ...s, productName: e.target.value }))
                            }
                          />
                        ) : (
                          pickName(p)
                        )}
                      </td>

                      <td className="p-2">
                        {isEditing ? (
                          <input
                            className="border rounded px-2 py-1 w-full"
                            value={editingForm.price}
                            onChange={(e) =>
                              setEditingForm((s) => ({ ...s, price: e.target.value }))
                            }
                            inputMode="numeric"
                          />
                        ) : (
                          p?.price ?? ""
                        )}
                      </td>

                      <td className="p-2">
                        {isEditing ? (
                          /* 💡 높이를 80(약 320px)으로 키우고 너비를 확보하여 시인성 개선 */
                          <div className="border rounded-lg p-2 max-h-80 min-w-[220px] overflow-y-auto bg-gray-50 shadow-inner border-blue-200">
                            <div className="text-[10px] font-bold text-blue-500 mb-2 px-1 uppercase tracking-tight">
                              카테고리 수정 모드
                            </div>
                            {categoryTree.map((node) => (
                              <CategoryTreeItem
                                key={node.id}
                                node={node}
                                selectedIds={editingForm.categoryId}
                                onChange={(id, checked) => handleCategoryToggle(id, checked, true)}
                              />
                            ))}
                          </div>
                        ) : (
                          /* 💡 평상시에도 가독성이 좋게 태그 스타일로 표시 */
                          <div className="flex flex-wrap gap-1 max-w-[200px]">
                            {pickCategoryName(p).split(", ").map((name, i) => (
                              <span key={i} className="bg-white border border-gray-200 text-gray-600 px-2 py-0.5 rounded-md text-[11px] shadow-sm">
                                {name}
                              </span>
                            ))}
                          </div>
                        )}
                      </td>

                      <td className="p-2">
                        {isEditing ? (
                          <textarea
                            className="border rounded px-2 py-1 w-full min-h-[60px]"
                            value={editingForm.content}
                            onChange={(e) =>
                              setEditingForm((s) => ({ ...s, content: e.target.value }))
                            }
                          />
                        ) : (
                          <div className="max-h-20 overflow-y-auto whitespace-pre-wrap text-xs text-gray-600">
                            {p?.content || "-"}
                          </div>
                        )}
                      </td>

                      <td className="p-2">
                        {isEditing ? (
                          <input
                            className="border rounded px-2 py-1 w-full"
                            value={editingForm.originName}
                            onChange={(e) =>
                              setEditingForm((s) => ({ ...s, originName: e.target.value }))
                            }
                          />
                        ) : (
                          p?.originName ?? ""
                        )}
                      </td>

                      <td className="p-2">
                        {isEditing ? (
                          <input
                            className="border rounded px-2 py-1 w-full"
                            type="file"
                            accept="image/*"
                            onChange={(e) =>
                              setEditingForm((s) => ({
                                ...s,
                                imageFile: e.target.files?.[0] ?? null,
                              }))
                            }
                          />
                        ) : (
                          p?.imageUrl ? (
                            <div className="w-16 h-16 border rounded overflow-hidden bg-gray-50">
                              <img
                                src={p.imageUrl 
                                  ? (p.imageUrl.startsWith('http') 
                                      ? p.imageUrl 
                                      : `http://localhost:8080${p.imageUrl.startsWith('/') ? '' : '/'}${p.imageUrl}`)
                                  : "https://via.placeholder.com/64?text=No+Image"
                                }
                                alt={pickName(p)}
                                className="w-16 h-16 object-cover cursor-zoom-in hover:opacity-80 transition-opacity" // 클릭 가능 표시
                                onClick={() => {
                                  // 클릭 시 현재 이미지 주소를 전체 주소로 만들어 저장
                                  const fullUrl = p.imageUrl.startsWith('http') 
                                    ? p.imageUrl 
                                    : `http://localhost:8080${p.imageUrl.startsWith('/') ? '' : '/'}${p.imageUrl}`;
                                  setPreviewImage(fullUrl);
                                }}
                                onError={(e) => { e.target.src = "https://via.placeholder.com/64?text=Error"; }}
                              />
                          </div>
                          ) : (
                            <span className="text-gray-400">-</span>
                          )
                        )}
                      </td>
                      <td className="p-2">
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
                              onClick={() => startEdit(p)}
                            >
                              수정
                            </button>
                            <button
                              type="button"
                              className="border rounded px-2 py-1"
                              onClick={() => onDelete(p)}
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
      {/* 이미지 모달 */}
      {previewImage && (
        <div 
          className="fixed inset-0 z-[100] flex items-center justify-center bg-black bg-opacity-80 p-4 cursor-zoom-out"
          onClick={() => setPreviewImage(null)}
        >
          <div className="relative max-w-4xl max-h-full" onClick={(e) => e.stopPropagation()}>
            {/* e.stopPropagation()을 추가하면 이미지를 클릭했을 땐 안 닫히고 배경만 눌러야 닫힙니다 */}
            <img 
              src={previewImage} 
              className="max-w-full max-h-[90vh] rounded-lg shadow-2xl border-4 border-white"
              alt="확대보기" 
            />
            <button 
              className="absolute -top-10 -right-0 text-white text-xl font-bold hover:text-gray-300"
              onClick={() => setPreviewImage(null)}
            >
              × 닫기
            </button>
          </div>
        </div>
      )}
    </div>
  );
}