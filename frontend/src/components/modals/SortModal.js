import React from 'react';
import { Modal } from 'react-bootstrap';
import { FaCheck } from 'react-icons/fa6';
import { useModalContext } from 'context/ModalContext';
import { useNavigate } from 'react-router-dom';

// ソートモーダルコンポーネント
// モーダルコンポーネント: SortModal
const SortModal = () => {
  const navigate = useNavigate();

  const { closeModal, modalProps } = useModalContext();
  const { sort, movieId = null, userId = null } = modalProps;

  // ソートオプションのリスト
  const options = [
    { key: 'like', label: 'いいね順' },
    { key: 'recent', label: '新着順' },
    { key: 'high', label: '高評価順' },
    { key: 'low', label: '低評価順' },
  ];

  // ソート選択ハンドラー
// イベント処理関数: handleSort
  const handleSort = (newSort) => {
    closeModal();
    // replace: true で履歴を置き換え
    if (movieId) {
      navigate(`/movie/${movieId}/reviews?sort=${newSort}`, { replace: true });
    } else if (userId) {
      navigate(`/user/${userId}/reviews?sort=${newSort}`, { replace: true });
    }
  };

  return (
    <Modal show={true} onHide={closeModal} centered animation={false} dialogClassName="custom-modal">
      <Modal.Header closeButton>
        <Modal.Title className="w-100 text-center">並び替え基準</Modal.Title>
      </Modal.Header>

      <Modal.Body className="py-0">
        {options.map((opt, idx) => {
          const isLast = idx === options.length - 1;
          return (
            <div
              key={opt.key}
              onClick={() => handleSort(opt.key)}
              style={{
                cursor: 'pointer',
                padding: '1rem',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                borderBottom: isLast ? 'none' : '1px solid rgb(73, 80, 87)',
              }}
            >
              <span>{opt.label}</span>
              {opt.key === sort && <FaCheck />}
            </div>
          );
        })}
      </Modal.Body>
    </Modal>
  );
};

export default SortModal;
