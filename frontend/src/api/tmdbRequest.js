// 映画APIのエンドポイント定義
const requests = {
  fetchNowPlaying: '/movie/now_playing?&language=ja-JP&page=1&region=JP',
  fetchAnimation:
    '/discover/movie?include_adult=false&include_video=false&language=ja-JP&page=1&primary_release_date.gte=2010-01-01&region=ja-JP&sort_by=popularity.desc&with_genres=16&with_original_language=ja',
  fetchMoviesByMokotoShinkai:
    '/discover/movie?include_adult=false&include_video=false&language=ja-JP&page=1&sort_by=popularity.desc&with_crew=74091&with_original_language=ja',
  fetchMoviesBySuzuHirose:
    '/discover/movie?include_adult=false&include_video=false&language=ja-JP&page=1&sort_by=popularity.desc&with_cast=1454976&with_original_language=ja',
  fetchSearchMovies: (query) =>
    `/search/movie?query=${encodeURIComponent(query)}&include_adult=false&language=ja-JP&page=1&region=JP`,
};

export default requests;
