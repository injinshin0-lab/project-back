// src/pages/admin/AdminCategoriesPage.jsx
import { useEffect, useMemo, useState } from "react";
import useAdminCategoryStore from "@/stores/adminCategoryStore";

function buildTree(list) {
  const map = new Map();
  const roots = [];

  (list || []).forEach((c) => {
    map.set(c._id, { ...c, children: [] });
  });

  map.forEach((node) => {
    const pid = node._parentId;
    if (pid && map.has(pid)) {
      map.get(pid).children.push(node);
    } else {
      roots.push(node);
    }
  });

  // 보기 좋게 정렬(이름 기준) - 필요 없으면 제거 가능
  const sortRec = (nodes) => {
    nodes.sort((a, b) => String(a._name).localeCompare(String(b._name)));
    nodes.forEach((n) => sortRec(n.children));
  };
  sortRec(roots);

  return roots;
}

function CategoryRow({
  node,
  depth,
  expanded,
  toggleExpand,
  onStartEdit,
  onDelete,
  isEditing,
  editingName,
  setEditingName,
  onSubmitEdit,
  onCancelEdit,
}) {
  const hasChildren = node.children?.length > 0;
  const isOpen = expanded.has(node._id);

  return (
    <>
      <tr className="border-b">
        <td className="p-2">{node._id}</td>
        <td className="p-2">
          <div className="flex items-center gap-2">
            <div style={{ width: depth * 14 }} />
            {hasChildren ? (
              <button
                type="button"
                className="border rounded px-2 py-0.5 text-xs"
                onClick={() => toggleExpand(node._id)}
                title="펼치기/접기"
              >
                {isOpen ? "−" : "+"}
              </button>
            ) : (
              <div style={{ width: 26 }} />
            )}

            <div className="flex-1">
              {isEditing ? (
                <input
                  className="border rounded px-2 py-1 w-full"
                  value={editingName}
                  onChange={(e) => setEditingName(e.target.value)}
                />
              ) : (
                <span>{node._name}</span>
              )}
            </div>
          </div>
        </td>
        <td className="p-2">{node._parentId ?? ""}</td>
        <td className="p-2">{node._level ?? ""}</td>
        <td className="p-2">
          {isEditing ? (
            <div className="flex gap-2">
              <button
                type="button"
                className="border rounded px-2 py-1"
                onClick={onSubmitEdit}
              >
                저장
              </button>
              <button
                type="button"
                className="border rounded px-2 py-1"
                onClick={onCancelEdit}
              >
                취소
              </button>
            </div>
          ) : (
            <div className="flex gap-2">
              <button
                type="button"
                className="border rounded px-2 py-1"
                onClick={() => onStartEdit(node)}
              >
                수정
              </button>
              <button
                type="button"
                className="border rounded px-2 py-1"
                onClick={() => onDelete(node)}
              >
                삭제
              </button>
            </div>
          )}
        </td>
      </tr>

      {hasChildren && isOpen &&
        node.children.map((child) => (
          <CategoryRow
            key={child._id}
            node={child}
            depth={depth + 1}
            expanded={expanded}
            toggleExpand={toggleExpand}
            onStartEdit={onStartEdit}
            onDelete={onDelete}
            isEditing={isEditing && false}
            editingName={editingName}
            setEditingName={setEditingName}
            onSubmitEdit={onSubmitEdit}
            onCancelEdit={onCancelEdit}
          />
        ))}
    </>
  );
}

export default function AdminCategoriesPage() {
  const { list, loading, fetch, create, update, remove } =
    useAdminCategoryStore();

  // 추가 폼
  const [createForm, setCreateForm] = useState({
    categoryName: "",
    parentId: "",
  });

  // 트리 펼침 상태
  const [expanded, setExpanded] = useState(new Set());

  // 수정 상태
  const [editingId, setEditingId] = useState(null);
  const [editingName, setEditingName] = useState("");

  useEffect(() => {
    fetch();
  }, [fetch]);

  const tree = useMemo(() => buildTree(list), [list]);

  const flatOptions = useMemo(() => {
    // 부모 선택 드롭다운용(전부)
    return (list || []).slice().sort((a, b) => String(a._name).localeCompare(String(b._name)));
  }, [list]);

  const toggleExpand = (id) => {
    setExpanded((prev) => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id);
      else next.add(id);
      return next;
    });
  };

  const onSubmitCreate = async (e) => {
    e.preventDefault();

    const categoryName = createForm.categoryName.trim();
    if (!categoryName) {
      alert("카테고리명을 입력해주십시오");
      return;
    }

    const parentId = createForm.parentId ? Number(createForm.parentId) : null;

    // ✅ 백엔드 DTO 기준 필드명: categoryName, parentId
    await create({ categoryName, parentId });

    setCreateForm({ categoryName: "", parentId: "" });
  };

  const onStartEdit = (node) => {
    setEditingId(node._id);
    setEditingName(node._name || "");
  };

  const onCancelEdit = () => {
    setEditingId(null);
    setEditingName("");
  };

  const onSubmitEdit = async () => {
    const categoryName = editingName.trim();
    if (!categoryName) {
      alert("수정할 이름을 입력해주십시오");
      return;
    }
    await update(editingId, { categoryName });
    onCancelEdit();
  };

  const onDelete = async (node) => {
    const ok = window.confirm(
      "정말 삭제하시겠습니까?\n(하위 카테고리가 있으면 서버에서 삭제가 막힐 수 있습니다)"
    );
    if (!ok) return;
    await remove(node._id);
  };

  return (
    <div className="p-4">
      <h1 className="text-xl font-semibold mb-4">관리자 · 카테고리 관리</h1>

      {/* 생성 폼 */}
      <form
        onSubmit={onSubmitCreate}
        className="border rounded-lg p-3 mb-4 flex flex-col gap-2"
      >
        <div className="flex gap-2 items-center">
          <label className="w-24 text-sm">이름</label>
          <input
            className="border rounded px-2 py-1 flex-1"
            value={createForm.categoryName}
            onChange={(e) =>
              setCreateForm((p) => ({ ...p, categoryName: e.target.value }))
            }
            placeholder="카테고리 이름"
          />
        </div>

        <div className="flex gap-2 items-center">
          <label className="w-24 text-sm">부모</label>
          <select
            className="border rounded px-2 py-1 flex-1"
            value={createForm.parentId}
            onChange={(e) =>
              setCreateForm((p) => ({ ...p, parentId: e.target.value }))
            }
          >
            <option value="">(최상위)</option>
            {flatOptions.map((c) => (
              <option key={c._id} value={c._id}>
                {c._name} (ID: {c._id})
              </option>
            ))}
          </select>
        </div>

        <div className="flex justify-end">
          <button className="border rounded px-3 py-1" type="submit" disabled={loading}>
            추가
          </button>
        </div>
      </form>

      {/* 목록 */}
      <div className="border rounded-lg overflow-hidden">
        <div className="px-3 py-2 border-b flex items-center justify-between">
          <div className="text-sm">
            총 <b>{list?.length ?? 0}</b>개
          </div>
          <div className="flex gap-2">
            <button
              type="button"
              className="border rounded px-3 py-1 text-sm"
              onClick={() => fetch()}
              disabled={loading}
            >
              새로고침
            </button>
            <button
              type="button"
              className="border rounded px-3 py-1 text-sm"
              onClick={() => setExpanded(new Set(list.map((c) => c._id)))}
              disabled={loading}
            >
              전체 펼치기
            </button>
            <button
              type="button"
              className="border rounded px-3 py-1 text-sm"
              onClick={() => setExpanded(new Set())}
              disabled={loading}
            >
              전체 접기
            </button>
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b">
                <th className="p-2 text-left">ID</th>
                <th className="p-2 text-left">이름</th>
                <th className="p-2 text-left">부모ID</th>
                <th className="p-2 text-left">레벨</th>
                <th className="p-2 text-left">작업</th>
              </tr>
            </thead>
            <tbody>
              {loading && (
                <tr>
                  <td className="p-4 text-center" colSpan={5}>
                    불러오는 중...
                  </td>
                </tr>
              )}

              {!loading && tree.length === 0 && (
                <tr>
                  <td className="p-4 text-center" colSpan={5}>
                    카테고리가 없습니다.
                  </td>
                </tr>
              )}

              {!loading &&
                tree.map((node) => {
                  const isEditing = editingId === node._id;
                  return (
                    <CategoryRow
                      key={node._id}
                      node={node}
                      depth={0}
                      expanded={expanded}
                      toggleExpand={toggleExpand}
                      onStartEdit={onStartEdit}
                      onDelete={onDelete}
                      isEditing={isEditing}
                      editingName={editingName}
                      setEditingName={setEditingName}
                      onSubmitEdit={onSubmitEdit}
                      onCancelEdit={onCancelEdit}
                    />
                  );
                })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}