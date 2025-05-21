import React, { useEffect, useState, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { Spinner } from 'react-bootstrap';
import MainNavbar from 'components/layouts/Navbar/MainNavBar';
import Footer from 'components/layouts/footer/Footer';
import MovieDetail from 'components/common/MovieDetail';
import ReviewRow from 'components/common/ReviewRow';
import CastRow from 'components/common/CastRow';
import { api } from 'api/apiClient';

// ページコンポーネント: MovieDetailPage
const MovieDetailPage = () => {
  const { movieId } = useParams(); // URLパラメータからムービーIDを取得
  const [movie, setMovie] = useState(null); // 映画詳細データ保持
  const [reviews, setReviews] = useState([]); // プレビュー用レビューリスト保持
  const [isLoading, setIsLoading] = useState(true); // ロード状態

  // ページ初期化時とmovieId変更時にデータを取得
  useEffect(() => {
    const fetchMovieData = async () => {
      try {
        const movieResponse = await api.get(`/api/movie/${movieId}`);
        const reviewResponse = await api.get(`/api/review/movie/preview/${movieId}`);

        setMovie(movieResponse.data);
        setReviews(reviewResponse.data);
      } catch (err) {
        console.error('映画取得エラー：', err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchMovieData();
  }, [movieId]);

  const handleReviewRegistered = useCallback(async () => {
    const reviewResponse = await api.get(`/api/review/movie/preview/${movieId}`);
    setReviews(reviewResponse.data);
  }, [movieId]);

  return (
    <div className="movie-detail">
      <MainNavbar />
      {isLoading ? (
        <div className="d-flex justify-content-center align-items-center vh-100">
          <Spinner animation="border" variant="danger" />
        </div>
      ) : (
        <>
          <MovieDetail movie={movie} onReviewRegistered={handleReviewRegistered} />
          <ReviewRow movie={movie} review={reviews} onReviewRegistered={handleReviewRegistered} />
          <CastRow movie={movie} />
          <Footer />
        </>
      )}
    </div>
  );
};

export default MovieDetailPage;
