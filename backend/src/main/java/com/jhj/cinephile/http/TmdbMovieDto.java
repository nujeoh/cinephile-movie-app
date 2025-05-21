package com.jhj.cinephile.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbMovieDto {

    @JsonProperty("backdrop_path")
    private String backdropPath;

    private List<GenreDto> genres;

    private long id;

    private String overview;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("origin_country")
    private List<String> originCountry;

    @JsonProperty("release_date")
    private LocalDate releaseDate;

    @JsonProperty("vote_average")
    private double voteAverage;

    private int runtime;

    private String title;

    private CreditsDto credits;

    @JsonProperty("release_dates")
    private ReleaseDates releaseDates;

    @Getter
    @Setter
    public static class ReleaseDates {
        private List<CountryRelease> results;
    }

    @Getter
    @Setter
    public static class CountryRelease {
        @JsonProperty("iso_3166_1")
        private String countryCode;

        @JsonProperty("release_dates")
        private List<ReleaseDateInfo> releaseDates;
    }

    @Getter
    @Setter
    public static class ReleaseDateInfo {
        private String certification;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GenreDto {
        private int id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreditsDto {
        private List<CastDto> cast;
        private List<CrewDto> crew;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CastDto {
        private String name;
        private String character;
        @JsonProperty("profile_path")
        private String profilePath;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CrewDto {
        private String name;
        private String department;
    }
}