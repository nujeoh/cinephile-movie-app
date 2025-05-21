import React from 'react';
import { Modal, Button, Row, Col } from 'react-bootstrap';
import { useModalContext } from 'context/ModalContext';
import { useNavigate } from 'react-router-dom';
import { useAuthContext } from 'context/AuthContext';
import { toast } from 'react-toastify';

// モーダルコンポーネント: ReviewDeleteModal
const ReviewDeleteModal = () => {
  const navigate = useNavigate();
  const { closeModal } = useModalContext();
  const { setIsAuthenticated } = useAuthContext(); // 認証状態管理

  // ログアウト処理
// イベント処理関数: handleLogout
  const handleLogout = () => {
    localStorage.removeItem('token');
    setIsAuthenticated(false);
    closeModal();
    navigate('/');
    toast.success('ログアウトしました！');
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
        <p className="mb-0">ログアウトしてもよろしいですか？</p>
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
              onClick={handleLogout}
              className="w-100 h-100 rounded-0 d-flex justify-content-center align-items-center"
            >
              ログアウトする
            </Button>
          </Col>
        </Row>
      </Modal.Footer>
    </Modal>
  );
};

export default ReviewDeleteModal;
