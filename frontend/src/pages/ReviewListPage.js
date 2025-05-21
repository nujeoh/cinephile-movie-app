import React, { useEffect, useState } from 'react';
import { useParams, useSearchParams } from 'react-router-dom';
import { Spinner } from 'react-bootstrap';
import ReviewList from 'components/common/ReviewList';
import MainNavbar from 'components/layouts/Navbar/MainNavBar';
import Footer from 'components/layouts/footer/Footer';
import SortRow from 'components/common/SortRow';
import { api } from 'api/apiClient';

// ページコンポーネント: ReviewListPage
const ReviewListPage = () => {
  const { movieId, userId } = useParams();

  // ソート基準パラメータ（like, recent など）
  const [searchParams] = useSearchParams();
  const sort = searchParams.get('sort') || 'like';

  // 初期レビュー、ページ番号、次ページの有無、ロード状態
  const [initialReviews, setInitialReviews] = useState([]);
  const [reviewPage, setReviewPage] = useState(1);
  const [reviewHasNext, setReviewHasNext] = useState(true);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // レビューリストを取得
    const fetchReviews = async () => {
      let fetchUrl;
      if (movieId) {
        fetchUrl = `/api/review/movie/all/${movieId}?sort=${sort}&page=0&size=5`;
      } else if (userId) {
        fetchUrl = `/api/review/user/all?sort=${sort}&page=0&size=5`;
      } else {
        console.error('re');
        return;
      }

      try {
        const response = await api.get(fetchUrl);

        setInitialReviews(response.data.content);
        setReviewHasNext(!response.data.last);
        setReviewPage(1);
      } catch (err) {
        console.error('レビュー取得失敗:', err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchReviews();
  }, [movieId, userId, sort]);

  return (
    <div className="review-list pt-5">
      <MainNavbar />

      {isLoading ? (
        <div className="d-flex justify-content-center align-items-center vh-100">
          <Spinner animation="border" variant="danger" />
        </div>
      ) : (
        <>
          <SortRow movieId={movieId} userId={userId} sort={sort} />
          <ReviewList
            movieId={movieId}
            userId={userId}
            sort={sort}
            initialReviews={initialReviews}
            initialPage={reviewPage}
            initialHasNext={reviewHasNext}
          />

          <Footer />
        </>
      )}
    </div>
  );
};

export default ReviewListPage;
