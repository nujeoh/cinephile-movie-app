// src/components/modals/LoginModal.js
import React, { useState, useEffect } from 'react';
import { Button, Modal, Form } from 'react-bootstrap';
import { useAuthContext } from 'context/AuthContext';
import { MODAL_TYPES, useModalContext } from 'context/ModalContext';
import { api } from 'api/apiClient';
import { toast } from 'react-toastify';
import 'styles/Modals.css';

// モーダルコンポーネント: LoginModal
const LoginModal = () => {
  const { closeModal, openModal } = useModalContext();
  const { setIsAuthenticated } = useAuthContext();

  // フォーム入力データ
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });
  const [errors, setErrors] = useState({}); // バリデーションエラー
  const [submitted, setSubmitted] = useState(false); // 送信済フラグ
  const [isLoading, setIsLoading] = useState(false); // ローディングフラグ

  // 初期化：モーダル開閉でリセット
  useEffect(() => {
    setFormData({ email: '', password: '' });
    setErrors({});
    setSubmitted(false);
  }, []);

  // 入力検証関数
// 関数: validateForm
  const validateForm = () => {
    const newErrors = {};

    // メールアドレス形式チェック
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!formData.email || !emailRegex.test(formData.email)) {
      newErrors.email = '有効なメールアドレスを入力してください。';
    }

    // パスワード強度チェック (英字＋数字10文字以上)
    const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{10,}$/;
    if (!formData.password || !pwRegex.test(formData.password)) {
      newErrors.password = '10文字以上、英字と数字を含めてください。';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // 入力変更ハンドラー
// イベント処理関数: handleChange
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // フォーム送信ハンドラー
  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitted(true);

    if (!validateForm()) return; // バリデーションNGなら送信中止

    setIsLoading(true);
    try {
      const { data } = await api.post('/api/user/login', formData);
      // トークン保存＆認証状態更新
      localStorage.setItem('token', data.token);
      setIsAuthenticated(true);
      closeModal();
      toast.success('ログインに成功しました！');
    } catch (fieldErrors) {
      setErrors(fieldErrors);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Modal show onHide={closeModal} centered animation={false} dialogClassName="custom-modal">
      <Modal.Header className="border-0 d-flex flex-column align-items-center w-100">
        <img src="/logo.png" width="200" height="60" alt="logo" className="mb-2" />
        <Modal.Title className="text-center w-100 pt-3">ログイン</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <Form noValidate onSubmit={handleSubmit}>
          <Form.Group>
            <Form.Control
              type="email"
              name="email"
              placeholder="メールアドレス"
              autoFocus
              className="mb-2"
              value={formData.email}
              onChange={handleChange}
              isInvalid={submitted && !!errors.email}
              isValid={submitted && formData.email && !errors.email}
            />
            <Form.Control.Feedback type="invalid" className="mb-2">
              {errors.email}
            </Form.Control.Feedback>

            <Form.Control
              type="password"
              name="password"
              placeholder="パスワード"
              className="mb-2"
              value={formData.password}
              onChange={handleChange}
              isInvalid={submitted && !!errors.password}
              isValid={submitted && formData.password && !errors.password}
            />
            <Form.Control.Feedback type="invalid" className="mb-2">
              {errors.password}
            </Form.Control.Feedback>

            <Button type="submit" variant="secondary" className="login-btn w-100 border-0 mt-2" disabled={isLoading}>
              {isLoading ? 'ログイン中...' : 'ログイン'}
            </Button>
          </Form.Group>
        </Form>
      </Modal.Body>

      <div className="text-center pb-3">
        アカウントをお持ちでないですか？&nbsp;
        <span className="text-primary" style={{ cursor: 'pointer' }} onClick={() => openModal(MODAL_TYPES.SIGNUP)}>
          新規登録
        </span>
      </div>
    </Modal>
  );
};

export default LoginModal;
