import axios from 'axios';

// TMDB API 接続用 Axios インスタンスを生成
const tmdbApiClient = axios.create({
  // ベースURLを設定
  baseURL: 'https://api.themoviedb.org/3',
  headers: {
    // 認証ヘッダー: Bearerトークンを挿入
    Authorization:
      'Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkZWRiZWY4ZmRhMmQwMWU0MzUxM2M3ZGY3ZGVmOTFkYiIsIm5iZiI6MTczNjg2MTUzMi43MjgsInN1YiI6IjY3ODY2NzVjOTBmNDJjMzI4MzdiYTMwNCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.FAQI_9xNxW6U5PD-DpAE5-DnwNnwlhZ7T5KNx9kCztM',
    // レスポンス形式設定: JSON
    accept: 'application/json',
  },
});

export default tmdbApiClient;
