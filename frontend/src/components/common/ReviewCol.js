import React, { useState } from 'react';
import { Card, Container, Row, Col, Button, Image } from 'react-bootstrap';
import { BiSolidLike } from 'react-icons/bi';
import { BsChatFill, BsChevronRight, BsPencilFill, BsStarFill } from 'react-icons/bs';
import { Link } from 'react-router-dom';
import { useAuthContext } from 'context/AuthContext';
import { api } from 'api/apiClient';
import { VscCommentDraft } from 'react-icons/vsc';

// 関数: ReviewCol
const ReviewCol = ({ review, user }) => {
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
  return (
    <Container fluid="md" className="my-5">
      <Row className="mb-1" style={{ maxWidth: '600px', margin: 'auto' }}>
        <Col>
          <span className="h5 d-flex align-items-center">
            私が投稿した感想・評価&nbsp;&nbsp;
            <BsPencilFill />
          </span>
        </Col>
        <Col xs="auto d-flex align-items-center">
          {reviews.length === 3 && (
            <Link to={`/user/${user.id}/reviews`} className="text-decoration-none text-light">
              もっと見る <BsChevronRight />
            </Link>
          )}
        </Col>
      </Row>
      {reviews.length > 0 ? (
        <Row className="g-3">
          {reviews.map((review, idx) => (
            <Col key={review.id} xs={12} className="mt-4">
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
      ) : (
        <Row>
          <Col className="text-center">
            <div className="text-center text-muted pt-3 pb-2">
              <VscCommentDraft style={{ fontSize: '80px' }} />
            </div>
            <div className="text-center text-muted pb-2">まだ投稿した感想・評価がありません。</div>
          </Col>
        </Row>
      )}
    </Container>
  );
};

export default ReviewCol;
