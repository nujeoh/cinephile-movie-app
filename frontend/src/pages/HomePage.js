import React, { useEffect, useState } from 'react';
import tmdbApiClient from 'api/tmdbApiClient';
import requests from 'api/tmdbRequest';
import MainNavbar from 'components/layouts/Navbar/MainNavBar';
import MovieRow from 'components/common/MovieRow';
import Footer from 'components/layouts/footer/Footer';
import { Spinner } from 'react-bootstrap';

// ページコンポーネント: HomePage
const HomePage = () => {
  // ロード状態
  const [isLoading, setIsLoading] = useState(true);
  // 映画カテゴリー別データ保管
  const [movies, setMovies] = useState({
    nowPlaying: [],
    animation: [],
    shinkai: [],
    suzuHirose: [],
  });

  useEffect(() => {
    // TMDB APIから全カテゴリーの映画を取得
    const fetchAllMovies = async () => {
      try {
        const [nowPlaying, animation, shinkai, suzuHirose] = await Promise.all([
          tmdbApiClient.get(requests.fetchNowPlaying),
          tmdbApiClient.get(requests.fetchAnimation),
          tmdbApiClient.get(requests.fetchMoviesByMokotoShinkai),
          tmdbApiClient.get(requests.fetchMoviesBySuzuHirose),
        ]);

        setMovies({
          nowPlaying: nowPlaying.data.results,
          animation: animation.data.results,
          shinkai: shinkai.data.results,
          suzuHirose: suzuHirose.data.results,
        });
      } catch (err) {
        console.error('映画取得エラー:', err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchAllMovies();
  }, []);

  return (
    <div className="home">
      <MainNavbar />

      {isLoading ? (
        <div className="d-flex justify-content-center align-items-center vh-100">
          <Spinner animation="border" variant="danger" />
        </div>
      ) : (
        <>
          <MovieRow title="#アニメ #日本" movies={movies.animation} />
          <MovieRow title="上映中の映画" movies={movies.nowPlaying} />
          <MovieRow title="新海誠監督の作品" movies={movies.shinkai} />
          <MovieRow title="広瀬すずの出演作品" movies={movies.suzuHirose} />
          <Footer />
        </>
      )}
    </div>
  );
};

export default HomePage;
