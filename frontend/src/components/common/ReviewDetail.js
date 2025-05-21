import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Container, Row, Col, Image, Button } from 'react-bootstrap';
import { BsChat, BsStarFill } from 'react-icons/bs';
import { BiLike, BiSolidLike } from 'react-icons/bi';
import dayjs from 'utils/dayjs';
import { useAuthContext } from 'context/AuthContext';
import { MODAL_TYPES, useModalContext } from 'context/ModalContext';
import { api } from 'api/apiClient';
import { jwtDecode } from 'jwt-decode';
import { encodeId } from 'utils/hashids';

// レビュー詳細コンポーネント
// 関数: ReviewDetail
const ReviewDetail = ({ review, movie, onPosted, onReviewUpdated }) => {
  const { isAuthenticated } = useAuthContext(); // 認証状態
  const { openModal } = useModalContext(); // モーダル操作関数

// データ取得関数: getCurrentUserId
  const getCurrentUserId = () => {
    const token = localStorage.getItem('token');
    try {
      const decoded = jwtDecode(token);
      const userId = encodeId(decoded.sub);
      return userId;
    } catch (err) {
      console.error('トークンデコード失敗:', err);
      return null;
    }
  };

  // いいね状態管理
  const [liked, setLiked] = useState(review.liked);
  const [likeCount, setLikeCount] = useState(review.likeCount);

  // いいねトグルハンドラ
  const handleLike = async () => {
    if (!isAuthenticated) {
      openModal(MODAL_TYPES.AUTH_REQUIRED);
      return;
    }

    try {
      const { data } = await api.post(`/api/review/${review.id}/like`);

      setLiked(data.liked);
      setLikeCount(data.likeCount);
    } catch (err) {
      console.error('いいね処理失敗', err);
    }
  };

  return (
    <Container fluid="md" className="mt-5">
      <Row className="mb-3 px-2">
        <Col md={9} xs={9}>
          <div className="d-flex align-items-center gap-2 pb-2">
            <Image
              src={review.profileImageUrl || '/defaultProfile.png'}
              roundedCircle
              width={28}
              height={28}
              style={{ objectFit: 'cover' }}
            />
            <strong>{review.userName}</strong>
            <span className="text-muted">・ {dayjs(review.createdAt).fromNow()}</span>
          </div>
          <Link to={`/movie/${movie.id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
            <span className="mt-2 fs-4">{movie.title}</span>
          </Link>
          <div className="text-muted">{movie.releaseDate}</div>
          <div className="mt-2 mb-5 d-flex align-items-center">
            <BsStarFill />
            &nbsp;{review.rating}
          </div>
          <p className="mt-4">{review.content}</p>
        </Col>
        <Col md={3} xs={3} className="text-end">
          <Link to={`/movie/${movie.id}`}>
            <Image
              src={`https://image.tmdb.org/t/p/w154/${movie.posterPath}`}
              rounded
              fluid
              style={{ maxWidth: '80px' }}
            />
          </Link>
        </Col>
        <Col md={12} xs={12}>
          <div className="d-flex justify-content-between align-items-center mt-4" style={{ fontSize: '0.9rem' }}>
            <div className="text-muted">
              いいね！{likeCount}　コメント {review.commentCount || 0}
            </div>

            {isAuthenticated && review.userId === getCurrentUserId() && (
              <div className="d-flex gap-2 ms-3">
                <Button
                  size="sm"
                  variant="outline-lightgray"
                  onClick={() => openModal(MODAL_TYPES.REVIEW_UPDATE, { review, movie, onReviewUpdated })}
                >
                  編集
                </Button>
                <Button
                  size="sm"
                  variant="outline-lightgray"
                  onClick={() => openModal(MODAL_TYPES.REVIEW_DELETE, { review })}
                >
                  削除
                </Button>
              </div>
            )}
          </div>
        </Col>
      </Row>

      <Row className="border-top border-bottom py-2 mb-3 text-center">
        <Col className="border-end">
          <Button variant="dark" className="w-100" onClick={() => handleLike()}>
            {liked ? <BiSolidLike /> : <BiLike />}
            <span className={`ms-2 ${liked ? 'text-white fw-bold' : ''}`}>いいね！</span>
          </Button>
        </Col>
        <Col>
          <Button
            variant="dark"
            className="w-100"
            onClick={() =>
              isAuthenticated
                ? openModal(MODAL_TYPES.COMMENT, { review, onPosted })
                : openModal(MODAL_TYPES.AUTH_REQUIRED)
            }
          >
            <BsChat />
            &nbsp;&nbsp;コメント
          </Button>
        </Col>
      </Row>
    </Container>
  );
};

export default ReviewDetail;
