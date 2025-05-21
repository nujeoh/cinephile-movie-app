import { useEffect } from 'react';
import { isTokenExpired } from 'utils/isTokenExpired';
import { useAuthContext } from 'context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { useModalContext } from './ModalContext';

// 関数: AuthInitializer
const AuthInitializer = ({ children }) => {
  const navigate = useNavigate();
  const { setIsAuthenticated } = useAuthContext();
  const { closeModal } = useModalContext();

  useEffect(() => {
    // ⬇アプリ起動時にトークンの有効性をチェック
    const token = localStorage.getItem('token');
    if (token && !isTokenExpired(token)) {
      setIsAuthenticated(true);
    } else {
      setIsAuthenticated(false);
      localStorage.removeItem('token');
    }
  }, [setIsAuthenticated]);

  useEffect(() => {
    // 定期的にトークンの期限切れをチェック (5秒ごと)
    const interval = setInterval(() => {
      const token = localStorage.getItem('token');
      if (token && isTokenExpired(token)) {
        // セッション期限切れ時の処理
        localStorage.removeItem('token');
        setIsAuthenticated(false);
        closeModal();
        alert('セッションの有効期限が切れました。再度ログインしてください。');
        navigate('/');
      }
    }, 5000);

    return () => clearInterval(interval);
  }, [setIsAuthenticated, navigate, closeModal]);

  return children;
};

export default AuthInitializer;
