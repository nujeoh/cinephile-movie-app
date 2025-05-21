package com.jhj.cinephile.service;

import com.jhj.cinephile.dto.MovieResponse;
import com.jhj.cinephile.entity.embeddable.MovieCast;
import com.jhj.cinephile.exception.MovieNotFoundException;
import com.jhj.cinephile.http.TmdbMovieDto;
import com.jhj.cinephile.entity.MovieEntity;
import com.jhj.cinephile.mapper.MovieMapper;
import com.jhj.cinephile.repository.MovieRepository;
import com.jhj.cinephile.http.TmdbHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

// 映画詳細取得および保存サービス
@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final TmdbHttpClient tmdbHttpClient;
    private final MovieMapper mapper;

    /**
     * 映画IDで取得し存在しなければTMDB呼び出し後保存
     * 既存時は取得のみ実施
     * 保存時の重複キーエラーは無視して再取得
     */
    @Transactional
    public MovieResponse getOrCreateMovie(Long movieId) {

        // 1) DB取得
        Optional<MovieEntity> optMovie = movieRepository.findById(movieId);
        if (optMovie.isPresent()) {
            return mapper.toDto(optMovie.get());  // 既存の場合返却
        }

        // 2) TMDB API呼び出し
        String url = "/movie/" + movieId + "?append_to_response=credits,release_dates&language=ja-JP";
        TmdbMovieDto tmdb = tmdbHttpClient.get(url, TmdbMovieDto.class).getBody();
        if (tmdb == null) {
            // TMDBから情報が取得できない場合はBadRequestで処理
            throw new MovieNotFoundException("TMDBから映画情報を取得できません: " + movieId);
        }

        // 3) エンティティ変換
        MovieEntity toSave = MovieEntity.builder()
                .id(tmdb.getId())
                .title(tmdb.getTitle())
                .overview(tmdb.getOverview())
                .releaseDate(tmdb.getReleaseDate())
                .runtime(tmdb.getRuntime())
                .backdropPath(tmdb.getBackdropPath())
                .posterPath(tmdb.getPosterPath())
                .voteAverage(tmdb.getVoteAverage())
                .genres(tmdb.getGenres().stream()
                        .map(TmdbMovieDto.GenreDto::getName)
                        .collect(Collectors.toSet()))
                .originalCountries(new HashSet<>(tmdb.getOriginCountry()))
                .filmRating(tmdb.getReleaseDates().getResults().stream()
                        .filter(cr -> "JP".equals(cr.getCountryCode()))
                        .flatMap(cr -> cr.getReleaseDates().stream())
                        .map(rd -> rd.getCertification())
                        .filter(cert -> cert != null && !cert.isBlank())
                        .findFirst().orElse(null))
                .casts(tmdb.getCredits().getCast().stream()
                        .map(c -> new MovieCast(c.getName(), c.getCharacter(), c.getProfilePath()))
                        .collect(Collectors.toList()))
                .director(tmdb.getCredits().getCrew().stream()
                        .filter(c -> "Directing".equals(c.getDepartment()))
                        .map(c -> c.getName())
                        .findFirst().orElse(null))
                .build();

        // 4) DB保存 (重複時はログ出力後無視)
        try {
            movieRepository.save(toSave);
        } catch (DataIntegrityViolationException e) {
            log.warn("映画保存中に重複発生、無視します: {}", movieId);
        }

        // 5) 保存後再取得
        MovieEntity saved = movieRepository.findById(movieId)
                .orElseThrow(
                        () -> new MovieNotFoundException("保存後にも取得に失敗しました: " + movieId)
                );

        return mapper.toDto(saved);
    }
}
