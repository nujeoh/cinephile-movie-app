import React from 'react';
import { Container, Row, Col, Card } from 'react-bootstrap';
import { BsPeopleFill } from 'react-icons/bs';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Navigation } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css/scrollbar';

// CastRowコンポーネント
// 関数: CastRow
const CastRow = ({ movie }) => {
  // プロフィール画像があるキャストのみフィルタリング
  const filteredCast = movie.casts.filter((cast) => cast.profilePath);

  return (
    <Container fluid="md" className="my-5">
      <Row className="align-items-center mb-4 px-2">
        <Col>
          <span className="h5 d-flex align-items-center">
            <BsPeopleFill className="me-3" />『 {movie.title} 』 に出ているキャスト
          </span>
        </Col>
      </Row>

      {filteredCast.length > 0 ? (
        <Swiper
          className="px-2"
          modules={[Navigation]}
          navigation
          breakpoints={{
            0: { slidesPerView: 3, spaceBetween: 10, slidesPerGroup: 3 },
            768: { slidesPerView: 4, spaceBetween: 10, slidesPerGroup: 4 },
            992: { slidesPerView: 5, spaceBetween: 15, slidesPerGroup: 5 },
            1200: { slidesPerView: 6, spaceBetween: 15, slidesPerGroup: 6 },
          }}
        >
          {filteredCast.map((cast) => (
            <SwiperSlide key={`${cast.name}-${cast.character}`}>
              <Card className="border-0">
                <Card.Img
                  variant="top"
                  src={`http://image.tmdb.org/t/p/w185${cast.profilePath}`}
                  className="rounded-1"
                />
                <Card.Body className="px-0 pt-2 pb-0">
                  <Card.Title className="fs-6">{cast.name}</Card.Title>
                  <Card.Text>{cast.character} 役</Card.Text>
                </Card.Body>
              </Card>
            </SwiperSlide>
          ))}
        </Swiper>
      ) : (
        <Row>
          <Col className="text-center py-4">
            <p className="text-muted">この作品に関するキャスト情報はまだ登録されていません。</p>
          </Col>
        </Row>
      )}
    </Container>
  );
};

export default CastRow;
