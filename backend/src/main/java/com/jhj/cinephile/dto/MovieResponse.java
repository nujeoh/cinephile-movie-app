package com.jhj.cinephile.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
@Getter
public class MovieResponse {
    private String id;
    private String title;
    private String overview;
    private LocalDate releaseDate;
    private int runtime;
    private String backdropPath;
    private String posterPath;
    private LocalDateTime createdAt;
    private Set<String> genres;
    private Set<String> originalCountries;
    private List<CastResponse> casts;
    private String filmRating;
    private String director;
    private double voteAverage;
}