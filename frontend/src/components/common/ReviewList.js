import React, { useState, useEffect, useRef, useCallback } from 'react';
import { Card, Spinner, Container, Row, Col, Button, Image } from 'react-bootstrap';
import { BiSolidLike } from 'react-icons/bi';
import { BsChatFill, BsStarFill } from 'react-icons/bs';
import { api } from 'api/apiClient';
import { Link } from 'react-router-dom';
import { useAuthContext } from 'context/AuthContext';

// ReviewList コンポーネント
// 関数: ReviewList
const ReviewList = ({ movieId = null, userId = null, sort, initialReviews, initialPage, initialHasNext }) => {
  const { isAuthenticated } = useAuthContext();

  // ステート管理
  const [reviews, setReviews] = useState(initialReviews); // 現在表示中のレビューリスト
  const [loading, setLoading] = useState(false); // ローディングフラグ
  const pageRef = useRef(initialPage); // 現在のページ
  const hasNextRef = useRef(initialHasNext); // 次ページの有無
  const loadingRef = useRef(false); // 内部ローディングロック
  const observerRef = useRef(null); // インフィニットスクロール要素の参照

  // レビューを取得する関数
  const fetchReviews = useCallback(async () => {
    // 既に読み込み中、またはこれ以上取得すべきデータがない場合は終了
    if (loadingRef.current || !hasNextRef.current) return;

    loadingRef.current = true;
    setLoading(true);

    let fetchUrl = null;
    if (movieId) {
      fetchUrl = `/api/review/movie/all/${movieId}?sort=${sort}&page=${pageRef.current}&size=5`;
    } else if (userId) {
      fetchUrl = `/api/review/user/all?sort=${sort}&page=${pageRef.current}&size=5`;
    } else {
      console.error('');
      return;
    }

    try {
      const { data } = await api.get(fetchUrl);

      // 取得したレビューをを追加
      setReviews((prev) => [...prev, ...data.content]);

      // 次ページの有無を更新
      hasNextRef.current = !data.last;
      pageRef.current += 1;
    } catch (err) {
      console.error('レビュー取得失敗', err);
    } finally {
      loadingRef.current = false;
      setLoading(false);
    }
  }, [movieId, userId, sort]);

  // 無限スクロール設定
  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) fetchReviews();
      },
      { rootMargin: '0px 0px -200px 0px' }
    );
    const el = observerRef.current;
    if (el) observer.observe(el);

    return () => {
      if (el) observer.unobserve(el);
      observer.disconnect();
    };
  }, [fetchReviews]);

  // ソート変更時に初期化
  useEffect(() => {
    setReviews(initialReviews);
    pageRef.current = initialPage;
    hasNextRef.current = initialHasNext;
    loadingRef.current = false;
    window.scrollTo({ top: 0, behavior: 'instant' });
  }, [sort, initialReviews, initialPage, initialHasNext]);

  // いいねトグルハンドラ
  const handleLike = async (reviewId, idx) => {
    if (!isAuthenticated) return; // 認証なければ無視
    try {
      const { data } = await api.post(`/api/review/${reviewId}/like`);
      setReviews((prev) => {
        const next = [...prev];
        next[idx] = { ...next[idx], liked: data.liked, likeCount: data.likeCount };
        return next;
      });
    } catch (err) {
      console.error('いいね処理失敗', err);
    }
  };

  // 初期ローディングスピナー
  if (reviews.length === 0 && loading) {
    return (
      <div className="d-flex justify-content-center align-items-center vh-100">
        <Spinner animation="border" variant="danger" />
      </div>
    );
  }

  return (
    <Container fluid="md" className="mt-5">
      <Row className="g-3 px-2 pt-2">
        {reviews.map((review, idx) => (
          <Col key={review.id} xs={12} className="mt-3">
            <Card bg="dark" style={{ maxWidth: '600px', margin: 'auto' }}>
              <Link to={`/review/${review.id}`} className="text-decoration-none text-light">
                <Card.Header className="d-flex justify-content-between align-items-center">
                  <div className="d-flex align-items-center">
                    <Image
                      src={review.profileImageUrl || '/defaultProfile.png'}
                      roundedCircle
                      width={28}
                      height={28}
                      className="me-2"
                      style={{ objectFit: 'cover' }}
                    />
                    <span>{review.userName}</span>
                  </div>
                  <div className="d-flex align-items-center">
                    <BsStarFill />
                    <span className="ms-1">{review.rating}</span>
                  </div>
                </Card.Header>
                <Card.Body style={{ height: '120px', overflow: 'hidden' }}>
                  <p
                    style={{
                      display: '-webkit-box',
                      WebkitLineClamp: 3,
                      WebkitBoxOrient: 'vertical',
                      overflow: 'hidden',
                      textOverflow: 'ellipsis',
                    }}
                  >
                    {review.content}
                  </p>
                </Card.Body>
              </Link>
              <Card.Footer className="d-flex align-items-center">
                <BiSolidLike />
                &nbsp;{review.likeCount}
                &nbsp;&nbsp;&nbsp;
                <BsChatFill />
                &nbsp;{review.commentCount}
                {isAuthenticated && (
                  <Button
                    variant={review.liked ? 'danger' : 'outline-light'}
                    className="ms-auto border-0"
                    size="sm"
                    onClick={() => handleLike(review.id, idx)}
                  >
                    いいね！
                  </Button>
                )}
              </Card.Footer>
            </Card>
          </Col>
        ))}
      </Row>

      <div ref={observerRef} className="text-center py-3">
        {loading && <Spinner animation="border" variant="danger" />}
      </div>
    </Container>
  );
};

export default ReviewList;
