import React, { createContext, useContext, useState } from 'react';

const ModalContext = createContext();

export const MODAL_TYPES = {
  LOGIN: 'LOGIN',
  LOGOUT: 'LOGOUT',
  SIGNUP: 'SIGNUP',
  REVIEW: 'REVIEW',
  REVIEW_UPDATE: 'REVIEW_UPDATE',
  REVIEW_DELETE: 'REVIEW_DELETE',
  COMMENT: 'COMMENT',
  AUTH_REQUIRED: 'AUTH_REQUIRED',
  SORT: 'SORT',
};

// モーダルコンポーネント: ModalProvider
export const ModalProvider = ({ children }) => {
  // 開いているモーダルの種類を管理するステート
  const [modalType, setModalType] = useState(null);
  // モーダルに渡す追加プロパティ
  const [modalProps, setModalProps] = useState({});

  // モーダルコンポーネント: openModal
  const openModal = (type, props = {}) => {
    setModalType(type);
    setModalProps(props);
  };

  // モーダルを閉じる関数
  // モーダルコンポーネント: closeModal
  const closeModal = () => {
    setModalType(null);
    setModalProps({});
  };

  return (
    <ModalContext.Provider value={{ modalType, modalProps, openModal, closeModal }}>{children}</ModalContext.Provider>
  );
};

// コンテキスト用フック
export const useModalContext = () => useContext(ModalContext);
