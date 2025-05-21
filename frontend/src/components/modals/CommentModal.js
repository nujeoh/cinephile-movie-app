// src/components/modals/CommentModal.js
import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { useModalContext } from 'context/ModalContext';
import { toast } from 'react-toastify';
import { api } from 'api/apiClient';

// モーダルコンポーネント: CommentModal
const CommentModal = ({ review, onPosted }) => {
  const { closeModal } = useModalContext();

  // コメント入力のステート管理
  const [comment, setComment] = useState({
    reviewId: review.id,
    content: '',
  });

  // フォーム送信ハンドラー
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await api.post('/api/review/comment', comment);

      closeModal();
      toast.success('コメントを投稿しました！');
      onPosted(); //上位コンポーネントにコメント登録完了を通知
    } catch (err) {
      console.error('コメント投稿失敗', err);
    }
  };

  return (
    <Modal show={true} onHide={closeModal} centered animation={false} dialogClassName="custom-modal">
      <Modal.Header className="border-0 pb-2" closeButton>
        <Modal.Title className="fs-5">コメント</Modal.Title>
      </Modal.Header>
      <Modal.Body className="p-3">
        <Form onSubmit={handleSubmit}>
          <Form.Group>
            <Form.Control
              as="textarea"
              rows={12}
              maxLength={1000}
              placeholder="この感想について自由にご意見をお書きください。"
              value={comment.content}
              onChange={(e) => setComment({ ...comment, content: e.target.value })}
            />
            <div className="text-end text-muted mt-1">{comment.content.length} / 1000</div>
          </Form.Group>
          <Button
            type="submit"
            variant="secondary"
            className="login-btn w-100 border-0 mt-3"
            disabled={comment.content.trim().length === 0}
          >
            投稿する
          </Button>
        </Form>
      </Modal.Body>
    </Modal>
  );
};

export default CommentModal;
