import React from 'react';
import { Modal, Button, Row, Col } from 'react-bootstrap';
import { useModalContext } from 'context/ModalContext';
import { api } from 'api/apiClient';
import { useNavigate } from 'react-router-dom';

// モーダルコンポーネント: ReviewDeleteModal
const ReviewDeleteModal = ({ review }) => {
  const { closeModal } = useModalContext();
  const navigate = useNavigate();

  const handleDelete = async () => {
    try {
      await api.delete('/api/review/delete', {
        data: { userId: review.userId, reviewId: review.id },
      });
      closeModal();
      navigate(`/movie/${review.movieId}`, { replace: true });
    } catch (err) {
      console.error('削除失敗:', err);
    }
  };

  return (
    <Modal show={true} onHide={closeModal} centered animation={false} dialogClassName="custom-modal">
      <Modal.Header className="border-0 pb-0 pt-4">
        <Modal.Title className="w-100 text-center">確認</Modal.Title>
        <button
          type="button"
          className="btn-close position-absolute"
          style={{ right: '1rem', top: '1rem' }}
          onClick={closeModal}
        />
      </Modal.Header>

      <Modal.Body className="text-center">
        <p className="mb-0">レビューを削除してもよろしいですか？</p>
      </Modal.Body>

      <Modal.Footer className="p-0">
        <Row className="w-100 m-2">
          <Col xs={6} className="d-flex justify-content-center align-items-center border-end p-0">
            <Button
              variant="dark"
              onClick={closeModal}
              className="w-100 h-100 rounded-0 d-flex justify-content-center align-items-center"
            >
              キャンセル
            </Button>
          </Col>
          <Col xs={6} className="d-flex justify-content-center align-items-center p-0">
            <Button
              variant="dark"
              onClick={handleDelete}
              className="w-100 h-100 rounded-0 d-flex justify-content-center align-items-center"
            >
              削除する
            </Button>
          </Col>
        </Row>
      </Modal.Footer>
    </Modal>
  );
};

export default ReviewDeleteModal;
