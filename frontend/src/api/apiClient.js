// API 呼び出し用のAxiosクライアント設定とエラー処理

import axios from 'axios';
import { toast } from 'react-toastify';
import { showErrorToast } from 'utils/toastHelper';

// Axiosインスタンスを生成
const api = axios.create({
  baseURL: 'http://cinephile-app-dev-free.eba-mqph9p7r.ap-northeast-1.elasticbeanstalk.com', // バックエンドサーバーのURL
  headers: { 'Content-Type': 'application/json' }, // リクエストの基本ヘッダー設定
  timeout: 10000, // タイムアウト設定（10秒）
});

// リクエストインターセプター：JWTトークンを自動的にAuthorizationヘッダーに添付
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`; // トークン添付
    }
    return config;
  },
  (err) => Promise.reject(err) // リクエストエラー時に拒否
);

// エラーハンドラ関数（400バリデーションエラーは別途処理しない）
// イベント処理関数: handleApiError
function handleApiError(response) {
  const { status, data } = response;
  const serverMsg = data?.message || data?.error || null;

  switch (status) {
    case 401:
      showErrorToast('ログインが必要です。');
      break;
    case 403:
      showErrorToast('権限がありません。');
      break;
    case 404:
      showErrorToast('リソースが見つかりません。');
      break;
    case 409:
      showErrorToast(serverMsg);
      break;
    case 422:
      showErrorToast(serverMsg);
      break;
    default:
      showErrorToast('サーバーエラーが発生しました。');
  }
}

// レスポンスインターセプター：400バリデーションエラーはフォームで処理、その他のエラーはtoastで通知
api.interceptors.response.use(
  (res) => res,
  (err) => {
    const { response } = err;

    if (!response) {
      // ネットワークエラー
      toast.error('ネットワークエラーが発生しました。接続を確認してください。');
      return Promise.reject(err);
    }

    if (response.status === 400 && response.data && typeof response.data === 'object') {
      // 400バリデーションエラー：response.dataをそのまま返却
      return Promise.reject(response.data);
    }

    // その他のエラー処理
    handleApiError(response);
    return Promise.reject(err);
  }
);

export { api };
