import React from 'react';
import { useModalContext, MODAL_TYPES } from 'context/ModalContext';
import LoginModal from './LoginModal';
import SignUpModal from './SignUpModal';
import AuthRequiredModal from './AuthRequiredModal';
import ReviewModal from './ReviewModal';
import CommentModal from './CommentModal';
import SortModal from './SortModal';
import ReviewUpdateModal from './ReviewUpdateModal';
import ReviewDeleteModal from './ReviewDeleteModal';
import LogoutModal from './LogoutModal';

// モーダルコンポーネント: ModalManager
const ModalManager = () => {
  // 現在表示すべきモーダルのタイプとモーダルに渡すprops、モーダルを閉じる関数を取得
  const { modalType, modalProps, closeModal } = useModalContext();

  switch (modalType) {
    case MODAL_TYPES.AUTH_REQUIRED:
      // 認証が必要な機能利用時の案内モーダル
      return <AuthRequiredModal show onHide={closeModal} {...modalProps} />;

    case MODAL_TYPES.LOGIN:
      // ログインモーダル
      return <LoginModal show onHide={closeModal} {...modalProps} />;

    case MODAL_TYPES.LOGOUT:
      // ログアウトモーダル
      return <LogoutModal show onHide={closeModal} {...modalProps} />;

    case MODAL_TYPES.SIGNUP:
      // サインアップモーダル
      return <SignUpModal show onHide={closeModal} {...modalProps} />;

    case MODAL_TYPES.REVIEW:
      // レビューモーダル
      return <ReviewModal show onHide={closeModal} {...modalProps} />;

    case MODAL_TYPES.REVIEW_DELETE:
      // レビュー削除モーダル
      return <ReviewDeleteModal show onHide={closeModal} {...modalProps} />;

    case MODAL_TYPES.REVIEW_UPDATE:
      // レビュー編集モーダル
      return <ReviewUpdateModal show onHide={closeModal} {...modalProps} />;

    case MODAL_TYPES.COMMENT:
      // コメントモーダル
      return <CommentModal show onHide={closeModal} {...modalProps} />;

    case MODAL_TYPES.SORT:
      // 並び替え基準選択モーダル
      return <SortModal show onHide={closeModal} {...modalProps} />;
    default:
      // 並び替え基準選択モーダル
      return null;
  }
};

export default ModalManager;
