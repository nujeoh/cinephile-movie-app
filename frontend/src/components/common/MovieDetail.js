import React from 'react';
import 'styles/MovieDetail.css';
import { Card, Container, Image, Row, Col, Button } from 'react-bootstrap';
import { useAuthContext } from 'context/AuthContext';
import { MODAL_TYPES, useModalContext } from 'context/ModalContext';
import { BsPencilFill, BsStarFill } from 'react-icons/bs';

// 映画詳細表示コンポーネント
// 関数: MovieDetail
const MovieDetail = ({ movie, onReviewRegistered }) => {
  const { openModal } = useModalContext();
  const { isAuthenticated } = useAuthContext();

  // ジャンル情報: なければデフォルトメッセージ
  const genres = movie.genres.join(' · ') || 'ジャンル情報なし';

  // 制作国: なければデフォルトメッセージ
  const originCountries = movie.originalCountries?.join(', ') || '情報なし';

  // 映倫情報: なければデフォルトメッセージ
  const filmRating = movie.filmRating || '映倫情報なし';

  // 監督: なければデフォルトメッセージ
  const director = movie.director || '情報なし';

  return (
    <Card className="card p-0">
      <Card.Img
        src={movie.backdropPath ? `https://image.tmdb.org/t/p/original/${movie.backdropPath}` : '/defaultBackdrop.png'}
        alt={movie.title}
        className="backdrop-img"
      />
      <Card.ImgOverlay className="d-flex align-items-center Card-image-overlay p-2">
        <Container fluid="md">
          <Row className="d-flex align-items-center">
            <Col xs="12" md="auto" className="me-4 d-none d-md-block">
              <Image src={`https://image.tmdb.org/t/p/w300/${movie.posterPath}`} className="poster-img" rounded />
            </Col>
            <Col>
              <Row>
                <Col xs="12" className="mb-3">
                  <p className="fs-1 my-0 py-0">{movie.title}</p>
                  <p className="fs-6 my-0 py-0">
                    {movie.releaseDate} ({originCountries}) · {movie.runtime}分 · {filmRating}
                  </p>
                  <p className="fs-6 my-0 py-0">{genres}</p>
                </Col>
                <Col xs="12" className="mb-3">
                  <p className="fs-5 my-0 py-0 d-flex align-items-center">
                    評価&nbsp;&nbsp;
                    <BsStarFill />
                    &nbsp;{movie.voteAverage?.toFixed(1) || 'N/A'}&nbsp;&nbsp;&nbsp;
                    <Button
                      variant="outline-light"
                      size="sm"
                      onClick={() => {
                        isAuthenticated
                          ? openModal(MODAL_TYPES.REVIEW, { movie, onReviewRegistered })
                          : openModal(MODAL_TYPES.AUTH_REQUIRED);
                      }}
                    >
                      感想・評価を投稿&nbsp;&nbsp;
                      <BsPencilFill />
                    </Button>
                  </p>
                </Col>
                <Col xs="12" className="mb-3">
                  <p className="fs-4 my-0 py-0">あらすじ</p>
                  <p className="fs-6 my-0 py-0">{movie.overview || 'あらすじ情報なし'}</p>
                </Col>
                <Col xs="12">
                  <div className="fs-4 d-none d-md-block">監督</div>
                  <div className="d-none d-md-block">{director}</div>
                  <div className="d-block d-md-none">監督 : {director}</div>
                </Col>
              </Row>
            </Col>
          </Row>
        </Container>
      </Card.ImgOverlay>
    </Card>
  );
};

export default MovieDetail;
