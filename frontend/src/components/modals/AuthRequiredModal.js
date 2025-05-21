import React from 'react';
import { Modal, Button } from 'react-bootstrap';
import { useModalContext, MODAL_TYPES } from 'context/ModalContext';

// 会員限定機能利用促進モーダル
// モーダルコンポーネント: AuthRequiredModal
const AuthRequiredModal = () => {
  const { closeModal, openModal } = useModalContext();

  return (
    <Modal show={true} onHide={closeModal} centered animation={false} dialogClassName="custom-modal">
      <Modal.Header className="border-0 d-flex flex-column align-items-center w-100">
        <img src="/logo.png" width="200" height="60" alt="logo" className="mb-2" />
        <Modal.Title className="text-center w-100 pt-5 fs-5">この機能を利用するには、</Modal.Title>
        <Modal.Title className="text-center w-100 pb-4 fs-5">会員登録またはログインが必要です。</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Button
          variant="secondary"
          className="login-btn w-100 border-0 mt-2"
          onClick={() => openModal(MODAL_TYPES.LOGIN)}
        >
          ログイン
        </Button>
        <Button
          variant="secondary"
          className="login-btn w-100 border-0 mt-2"
          onClick={() => openModal(MODAL_TYPES.SIGNUP)}
        >
          新規登録
        </Button>
      </Modal.Body>
    </Modal>
  );
};

export default AuthRequiredModal;
