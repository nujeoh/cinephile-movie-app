import React from 'react';
import { Link } from 'react-router-dom';
import 'styles/MovieRow.css';
import { Container, Row, Col, Card } from 'react-bootstrap';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Navigation } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css/scrollbar';
import { BsSearch, BsStarFill } from 'react-icons/bs';
import { encodeId } from 'utils/hashids';

// MovieRowコンポーネント
// 関数: MovieRow
const MovieRow = ({ title, movies }) => {
  // レスポンシブ対応
  const breakpoints = {
    0: { slidesPerView: 3, spaceBetween: 10, slidesPerGroup: 3 },
    768: { slidesPerView: 3, spaceBetween: 10, slidesPerGroup: 3 },
    992: { slidesPerView: 4, spaceBetween: 15, slidesPerGroup: 4 },
    1200: { slidesPerView: 5, spaceBetween: 20, slidesPerGroup: 5 },
  };

  return (
    <Container fluid="md" className="my-5">
      <Row className="px-2">
        <Col className="mb-3">
          <span className="h5">{title}</span>
        </Col>
      </Row>

      <Swiper className="px-2" modules={[Navigation]} navigation breakpoints={breakpoints}>
        {movies.length > 0 ? (
          movies
            .filter((movie) => movie.poster_path)
            .map((movie) => {
              const vote = Number(movie.vote_average).toFixed(1);
              return (
                <SwiperSlide key={movie.id}>
                  <Link to={`/movie/${encodeId(movie.id)}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                    <Card className="border-0">
                      <Card.Img
                        variant="top"
                        src={`https://image.tmdb.org/t/p/w500/${movie.poster_path}`}
                        style={{ aspectRatio: '27/40' }}
                        className="rounded-1"
                        alt={movie.title}
                      />
                      <Card.Body className="px-0 pt-2 pb-0">
                        <Card.Title className="card-title h6 mb-1">
                          {movie.title.length > 16 ? `${movie.title.slice(0, 16)}...` : movie.title}
                        </Card.Title>
                        <Card.Text className="card-text mb-0">{movie.release_date}</Card.Text>
                        <Card.Text className="card-text d-flex align-items-center">
                          平均&nbsp;
                          <BsStarFill />
                          &nbsp;{vote}
                        </Card.Text>
                      </Card.Body>
                    </Card>
                  </Link>
                </SwiperSlide>
              );
            })
        ) : (
          <Row>
            <Col className="text-center">
              <div className="text-center text-muted pt-3 pb-4 pe-3">
                <BsSearch style={{ fontSize: '80px' }} />
              </div>
              <div className="text-center text-muted pb-2">検索結果が見つかりませんでした。</div>
            </Col>
          </Row>
        )}
      </Swiper>
    </Container>
  );
};

export default MovieRow;
