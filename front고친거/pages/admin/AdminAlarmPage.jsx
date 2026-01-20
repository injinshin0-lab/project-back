import React, { useState, useEffect } from "react";
import { sendAlarmApi, getAllAlarmsApi } from "@/api/adminAlarmApi";

const AdminAlarmPage = () => {
    const [type, setType] = useState('all');
    const [targetLoginId, setTargetLoginId] = useState('');
    const [message, setMessage] = useState(''); // 👈 message로 통일
    const [loading, setLoading] = useState(false);

    const [history, setHistory] = useState([]);
    const [totalCount, setTotalCount] = useState(0);
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 10;

    const totalPages = Math.ceil(totalCount / itemsPerPage)

    const fetchHistory = async (page = 1) => {
       try {
            const res = await getAllAlarmsApi(page, 10);
            setHistory(res.data.list || []);
            setTotalCount(res.data.totalCount || 0);
            setCurrentPage(page);
       } catch (error) {
            console.error("전체 내역 로드 실패:", error)
       }
    };

    useEffect(()=> {
        fetchHistory();
    }, []);

    const handleSend = async () => {
        if (!message.trim()) return alert("내용을 입력하세요.");
        if (type === 'specific' && !targetLoginId) return alert("회원 ID를 입력하세요");

        try {
            setLoading(true);
            await sendAlarmApi({
                userLoginId: type === 'all' ? null : targetLoginId,
                content: message
            });

            alert("알림이 전송되었습니다!");
            setMessage('');
            setTargetLoginId(''); // 초기화
            fetchHistory();

        } catch (error) {
            alert(error.response?.data?.message || "알림 전송에 실패했습니다");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-6 flex flex-col gap-8"> {/* 레이아웃 확장 */}
            
            {/* 1. 알림 발송 폼 (기존 코드) */}
            <div className="max-w-lg w-full mx-auto bg-white shadow rounded-lg border p-6">
                <h2 className="text-xl font-bold mb-6">📢 관리자 알림 발송</h2>
                {/* ... (기존 탭 메뉴와 입력창 코드들) ... */}
                <div className="flex mb-6 border-b">
                    <button onClick={() => setType('all')} className={`flex-1 pb-2 font-medium ${type === 'all' ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-500'}`}>전체 발송</button>
                    <button onClick={() => setType('specific')} className={`flex-1 pb-2 font-medium ${type === 'specific' ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-500'}`}>특정 회원 발송</button>
                </div>

                {type === 'specific' && (
                    <div className="mb-4">
                        <label className="block text-sm mb-1 font-semibold">대상 회원 아이디</label>
                        <input 
                            type="text" 
                            className="w-full p-2 border rounded bg-gray-50" 
                            value={targetLoginId} 
                            onChange={(e) => setTargetLoginId(e.target.value)} 
                            placeholder="유저 로그인 아이디 입력" 
                        />
                    </div>
                )}

                <div className="mb-6">
                    <label className="block text-sm mb-1 font-semibold">알림 메시지</label>
                    <textarea className="w-full p-3 border rounded h-40 resize-none bg-gray-50" value={message} onChange={(e) => setMessage(e.target.value)} placeholder="내용을 입력하세요." />
                </div>

                <button onClick={handleSend} disabled={loading} className={`w-full py-3 rounded-md font-bold text-white transition-colors ${loading ? 'bg-gray-400' : 'bg-blue-600 hover:bg-blue-700'}`}>
                    {loading ? "전송 중..." : "알림 전송하기"}
                </button>
            </div>

            {/* 2. 🔥 새로 추가: 발송 내역 테이블 */}
            <div className="bg-white shadow rounded-lg border p-6">
                <div className="flex justify-between items-center mb-4">
                    <h3 className="text-lg font-bold text-gray-800">📜 최근 알림 발송 내역</h3>
                    <button 
                        onClick={() => fetchHistory(1)} 
                        className="text-sm text-blue-600 hover:underline"
                    >
                        새로고침
                    </button>
                </div>
                
                <div className="overflow-x-auto">
                    <table className="w-full text-sm border-collapse">
                        <thead>
                            <tr className="bg-gray-100 border-b">
                                <th className="p-3 text-left">ID</th>
                                <th className="p-3 text-left">대상 회원 ID</th>
                                <th className="p-3 text-left">유형</th>
                                <th className="p-3 text-left w-1/2">내용</th>
                                <th className="p-3 text-left">발송시간</th>
                            </tr>
                        </thead>
                        <tbody>
                            {history.length > 0 ? (
                                history.map((alarm) => (
                                    <tr key={alarm.id} className="border-b hover:bg-gray-50 transition-colors">
                                        <td className="p-3 text-gray-500">{alarm.id}</td>
                                        <td className="p-3">
                                            {alarm.userCount > 1 || alarm.userId === 0 ? (
                                                <span className="px-2 py-1 bg-purple-100 text-purple-700 rounded-md font-bold text-[11px]">
                                                    📢 전체
                                                </span>
                                            ) : (
                                                <span className="font-semibold text-gray-700">
                                                    {alarm.userLoginId || `ID: ${alarm.userId}`}
                                                </span>
                                            )}
                                        </td>
                                        <td className="p-3"><span className="px-2 py-1 bg-blue-100 text-blue-700 rounded-full text-[10px]">{alarm.type}</span></td>
                                        <td className="p-3 text-gray-700">{alarm.content}</td>
                                        <td className="p-3 text-gray-400 text-xs">{alarm.createdAt}</td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="5" className="p-10 text-center text-gray-400">발송된 내역이 없습니다.</td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>

                {/* 페이지네이션 버튼 */}
                {totalPages > 1 &&(
                    <div className="flex justify-center items-center gap-2 mt-6">
                        <button 
                            disabled={currentPage === 1}
                            onClick={() => fetchHistory(currentPage - 1)}
                            className="px-3 py-1 border rounded hover:bg-gray-50 disabled:opacity-30"
                        >이전</button>

                        {[...Array(totalPages)].map((_, i) => (
                            <button
                                key={i + 1}
                                onClick={() => fetchHistory(i + 1)}
                                className={`px-3 py-1 border rounded ${currentPage === i + 1 ? 'bg-blue-600 text-white' : 'hover:bg-gray-50'}`}
                            >{i + 1}</button>
                        ))}

                        <button 
                            disabled={currentPage === totalPages}
                            onClick={() => fetchHistory(currentPage + 1)}
                            className="px-3 py-1 border rounded hover:bg-gray-50 disabled:opacity-30"
                        >다음</button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default AdminAlarmPage;