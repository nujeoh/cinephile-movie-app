import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import ReactStars from 'react-stars';
import { useModalContext } from 'context/ModalContext';
import { api } from 'api/apiClient';
import { toast } from 'react-toastify';

// モーダルコンポーネント: ReviewModal
const ReviewModal = ({ movie, onReviewRegistered }) => {
  const { closeModal } = useModalContext();

  // レビュー入力のステート管理
  const [review, setReview] = useState({
    movieId: movie.id,
    rating: 0,
    content: '',
  });

  // フォーム送信ハンドラー
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await api.post('/api/review/register', review);

      closeModal();
      toast.success('レビューを投稿しました！');
      onReviewRegistered();
    } catch (err) {
      console.error('レビュー投稿失敗', err);
    }
  };

  return (
    <Modal show={true} onHide={closeModal} centered animation={false} dialogClassName="custom-modal">
      <Modal.Header className="border-0 pb-0" closeButton>
        <Modal.Title className="fs-5">{movie.title}</Modal.Title>
      </Modal.Header>
      <Modal.Body className="p-3">
        <Form onSubmit={handleSubmit}>
          <Form.Group>
            <div className="d-flex align-items-center pb-2">
              <ReactStars
                count={5}
                value={review.rating}
                onChange={(newRating) => setReview({ ...review, rating: newRating })}
                size={30}
                half={true}
                color2="#f4c150"
                color1="#6c757d"
              />
            </div>
            <Form.Control
              as="textarea"
              rows={12}
              maxLength={1000}
              placeholder="この映画についてのご感想を自由にお書きください。"
              value={review.content}
              onChange={(e) => setReview({ ...review, content: e.target.value })}
            />
            <div className="text-end text-muted mt-1">{review.content.length} / 1000</div>
          </Form.Group>

          <Button
            type="submit"
            variant="secondary"
            className="login-btn w-100 border-0 mt-3"
            disabled={review.rating < 0.5 || review.content.trim().length === 0}
          >
            投稿する
          </Button>
        </Form>
      </Modal.Body>
    </Modal>
  );
};

export default ReviewModal;
