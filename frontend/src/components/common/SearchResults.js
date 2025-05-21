import React from 'react';
import { Container } from 'react-bootstrap';
import MovieRow from './MovieRow';

// SearchResults コンポーネント
// 関数: SearchResults
const SearchResults = ({ searchQuery, movies }) => {
  return (
    <Container fluid="md">
      <MovieRow title={`「 ${searchQuery} 」 の検索結果`} movies={movies} />
    </Container>
  );
};

export default SearchResults;
