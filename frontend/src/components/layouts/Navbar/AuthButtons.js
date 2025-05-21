import React from 'react';
import { Link } from 'react-router-dom';
import { Nav } from 'react-bootstrap';
import { useAuthContext } from 'context/AuthContext';
import { MODAL_TYPES, useModalContext } from 'context/ModalContext';
import { jwtDecode } from 'jwt-decode';
import { encodeId } from 'utils/hashids';

// 関数: AuthButtons
const AuthButtons = () => {
  const { openModal } = useModalContext(); // モーダル表示関数
  const { isAuthenticated } = useAuthContext(); // 認証状態管理

  // 保存されたJWTトークンからユーザーIDを取得
// データ取得関数: getUserId
  const getUserId = () => {
    const token = localStorage.getItem('token');
    if (!token) return null;
    try {
      const { sub } = jwtDecode(token);
      return sub;
    } catch {
      return null;
    }
  };

  // 認証済みならユーザーIDを取得
  const userId = isAuthenticated ? getUserId() : null;

  return (
    <Nav className="ms-auto">
      {isAuthenticated ? (
        <>
          <Nav.Link className="me-1" onClick={() => openModal(MODAL_TYPES.LOGOUT)}>
            ログアウト
          </Nav.Link>
          <Nav.Link as={Link} to={`/user/${encodeId(userId)}`} className="me-3" style={{ textDecoration: 'none' }}>
            マイページ
          </Nav.Link>
        </>
      ) : (
        <>
          <Nav.Link className="me-1" onClick={() => openModal(MODAL_TYPES.LOGIN)}>
            ログイン
          </Nav.Link>
          <Nav.Link className="me-3" onClick={() => openModal(MODAL_TYPES.SIGNUP)}>
            新規登録
          </Nav.Link>
        </>
      )}
    </Nav>
  );
};

export default AuthButtons;
