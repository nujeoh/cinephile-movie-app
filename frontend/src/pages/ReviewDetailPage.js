import React, { useEffect, useState, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import MainNavbar from 'components/layouts/Navbar/MainNavBar';
import Footer from 'components/layouts/footer/Footer';
import { Spinner } from 'react-bootstrap';
import ReviewDetail from 'components/common/ReviewDetail';
import CommentList from 'components/common/CommentList';
import { api } from 'api/apiClient';

// ページコンポーネント: ReviewDetailPage
const ReviewDetailPage = () => {
  // URLパラメータからreviewIdを取得
  const { reviewId } = useParams();

  const [review, setReview] = useState(null); // レビュー詳細を保持
  const [movie, setMovie] = useState(null); // 映画情報を保持
  const [initialComments, setInitialComments] = useState([]); // 初期コメントリスト
  const [commentPage, setCommentPage] = useState(1); // コメントページ番号
  const [commentHasNext, setCommentHasNext] = useState(true); // 次ページ有無フラグ
  const [isLoading, setIsLoading] = useState(true); // ロード状態

  // コメント取得関数：レビュー投稿後にリストをリフレッシュ
  const fetchComments = useCallback(async () => {
    try {
      const response = await api.get(`/api/review/${reviewId}/comments?page=0&size=10`);

      setInitialComments(response.data.content);
      setCommentHasNext(!response.data.last);
      setCommentPage(1);
    } catch (err) {
      console.error('コメント取得失敗', err);
    }
  }, [reviewId]);

  // 初回レンダー時にレビュー・映画情報・コメントを一度に取得
  useEffect(() => {
    const fetchAll = async () => {
      try {
        const reviewRes = await api.get(`/api/review/${reviewId}`);

        const [movieRes, commentRes] = await Promise.all([
          api.get(`/api/movie/${reviewRes.data.movieId}`),
          api.get(`/api/review/${reviewId}/comments?page=0&size=10`),
        ]);

        setReview(reviewRes.data);
        setMovie(movieRes.data);
        setInitialComments(commentRes.data.content);
        setCommentHasNext(!commentRes.data.last);
        setCommentPage(1);
      } catch (err) {
        console.error('レビュー取得失敗:', err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchAll();
  }, [reviewId]);

  const handleReviewUpdated = useCallback(async () => {
    const response = await api.get(`/api/review/${reviewId}`);
    setReview(response.data);
  }, [reviewId]);

  return (
    <div className="review-detail">
      <MainNavbar />
      {isLoading ? (
        <div className="d-flex justify-content-center align-items-center vh-100">
          <Spinner animation="border" variant="danger" />
        </div>
      ) : (
        <>
          <ReviewDetail review={review} movie={movie} onPosted={fetchComments} onReviewUpdated={handleReviewUpdated} />
          <CommentList
            reviewId={reviewId}
            initialComments={initialComments}
            initialPage={commentPage}
            initialHasNext={commentHasNext}
          />
          <Footer />
        </>
      )}
    </div>
  );
};

export default ReviewDetailPage;
