import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Container, Row, Col, Card, Button, Image } from 'react-bootstrap';
import { VscCommentDraft } from 'react-icons/vsc';
import { BsChatFill, BsChatRightDotsFill, BsChevronRight, BsPencilFill, BsStarFill } from 'react-icons/bs';
import { BiSolidLike } from 'react-icons/bi';
import { useAuthContext } from 'context/AuthContext';
import { useModalContext, MODAL_TYPES } from 'context/ModalContext';
import { api } from 'api/apiClient';

// ReviewRow コンポーネント
// 関数: ReviewRow
const ReviewRow = ({ movie, review, onReviewRegistered }) => {
  const { openModal } = useModalContext();
  const { isAuthenticated } = useAuthContext();
  const [reviews, setReviews] = useState(review);

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

  useEffect(() => {
    setReviews(review);
  }, [review]);

  return (
    <Container fluid="md" className="my-5">
      <Row className="align-items-center mb-4 px-2">
        <Col>
          <span className="h5 d-flex align-items-center">
            <BsChatRightDotsFill className="me-3" />『 {movie.title} 』 に投稿された感想・評価
          </span>
        </Col>
        <Col xs="auto d-flex align-items-center">
          {reviews.length === 3 && (
            <Link to={`/movie/${movie.id}/reviews`} className="text-decoration-none text-light">
              もっと見る <BsChevronRight />
            </Link>
          )}
        </Col>
      </Row>

      {reviews.length > 0 ? (
        <Row xs={1} sm={1} md={3} className="g-3 px-2">
          {reviews.map((review, idx) => (
            <Col key={review.id}>
              <Card bg="dark">
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
      ) : (
        <Row>
          <Col className="text-center">
            <div className="text-center text-muted pt-3 pb-2">
              <VscCommentDraft style={{ fontSize: '80px' }} />
            </div>
            <div className="text-center text-muted pb-2">最初の感想・評価を投稿してみましょう！</div>
            <Button
              variant="outline-lightgray"
              size="sm"
              className="mt-2"
              onClick={() => {
                isAuthenticated
                  ? openModal(MODAL_TYPES.REVIEW, { movie, onReviewRegistered })
                  : openModal(MODAL_TYPES.AUTH_REQUIRED);
              }}
            >
              感想・評価を投稿&nbsp;&nbsp;
              <BsPencilFill />
            </Button>
          </Col>
        </Row>
      )}
    </Container>
  );
};

export default ReviewRow;
