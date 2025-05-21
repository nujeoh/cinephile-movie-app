import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import ReactStars from 'react-stars';
import { useModalContext } from 'context/ModalContext';
import { api } from 'api/apiClient';
import { toast } from 'react-toastify';

// モーダルコンポーネント: ReviewUpdateModal
const ReviewUpdateModal = ({ movie, review, onReviewUpdated }) => {
  const { closeModal } = useModalContext();

  const [updateReview, setUpdateReview] = useState({
    userId: review.userId,
    reviewId: review.id,
    rating: review.rating,
    content: review.content,
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.put('/api/review/update', updateReview);
      closeModal();
      toast.success('レビューを編集しました！');
      onReviewUpdated();
    } catch (err) {
      console.error('レビュー編集失敗', err);
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
                value={updateReview.rating}
                onChange={(newRating) => setUpdateReview((prev) => ({ ...prev, rating: newRating }))}
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
              value={updateReview.content}
              onChange={(e) => setUpdateReview((prev) => ({ ...prev, content: e.target.value }))}
            />
            <div className="text-end text-muted mt-1">{updateReview.content.length} / 1000</div>
          </Form.Group>

          <Button
            type="submit"
            variant="secondary"
            className="login-btn w-100 border-0 mt-3"
            disabled={updateReview.rating < 0.5 || updateReview.content.trim().length === 0}
          >
            編集する
          </Button>
        </Form>
      </Modal.Body>
    </Modal>
  );
};

export default ReviewUpdateModal;
