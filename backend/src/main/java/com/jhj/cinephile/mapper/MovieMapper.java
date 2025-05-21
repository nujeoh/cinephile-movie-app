package com.jhj.cinephile.mapper;

import com.jhj.cinephile.dto.CastResponse;
import com.jhj.cinephile.dto.MovieResponse;
import com.jhj.cinephile.entity.MovieEntity;
import com.jhj.cinephile.util.HashidsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MovieMapper {

    private final HashidsService hashidsService;

    public  MovieResponse toDto(MovieEntity movie) {
        return MovieResponse.builder()
                .id(hashidsService.encode(movie.getId()))
                .title(movie.getTitle())
                .overview(movie.getOverview())
                .releaseDate(movie.getReleaseDate())
                .runtime(movie.getRuntime())
                .backdropPath(movie.getBackdropPath())
                .posterPath(movie.getPosterPath())
                .createdAt(movie.getCreatedAt())
                .genres(movie.getGenres())
                .originalCountries(movie.getOriginalCountries())
                .filmRating(movie.getFilmRating())
                .voteAverage(movie.getVoteAverage())
                .director(movie.getDirector())
                .casts(
                        movie.getCasts().stream()
                                .map(c -> CastResponse.builder()
                                        .name(c.getName())
                                        .character(c.getCharacter())
                                        .profilePath(c.getProfilePath())
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();
    }
}
