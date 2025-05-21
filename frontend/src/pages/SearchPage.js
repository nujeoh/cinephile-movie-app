import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import tmdbApiClient from 'api/tmdbApiClient';
import requests from 'api/tmdbRequest';
import MainNavbar from 'components/layouts/Navbar/MainNavBar';
import Footer from 'components/layouts/footer/Footer';
import SearchResults from 'components/common/SearchResults';
import { Spinner } from 'react-bootstrap';

// ページコンポーネント: SearchPage
const SearchPage = () => {
  const location = useLocation();
  // URLのクエリ文字列から検索語を取得
  const queryParams = new URLSearchParams(location.search);
  const searchQuery = queryParams.get('query');

  // ロード状態, 映画リスト
  const [isLoading, setIsLoading] = useState(true);
  const [movies, setMovies] = useState([]);

  useEffect(() => {
    const fetchMovieData = async () => {
      try {
        const response = await tmdbApiClient.get(requests.fetchSearchMovies(searchQuery));
        setMovies(response.data.results);
      } catch (err) {
        console.error('映画検索失敗:', err);
      } finally {
        setIsLoading(false);
      }
    };
    fetchMovieData();
  }, [searchQuery]);

  return (
    <div className="home">
      <MainNavbar searchQuery={searchQuery} />

      {isLoading ? (
        <div className="d-flex justify-content-center align-items-center vh-100">
          <Spinner animation="border" variant="danger" />
        </div>
      ) : (
        <>
          <SearchResults searchQuery={searchQuery} movies={movies} />
          <Footer />
        </>
      )}
    </div>
  );
};

export default SearchPage;
