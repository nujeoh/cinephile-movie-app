package com.jhj.cinephile.entity;

import com.jhj.cinephile.entity.embeddable.MovieCast;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "movies")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MovieEntity {

    @Id
    private Long id;  // TMDB映画固有ID

    @Column(length = 512)
    private String title;  // 映画タイトル

    @Column(columnDefinition = "text")
    private String overview;  // 映画概要

    private LocalDate releaseDate;  // 公開日
    private int runtime;  // 上映時間(分)

    @Column(length = 512)
    private String backdropPath;  // バックドロップ画像パス

    @Column(length = 512)
    private String posterPath;  // ポスター画像パス

    @Column(length = 128)
    private String director;  // 監督名

    @Column(length = 16)
    private String filmRating;  // 映画等級

    private double voteAverage;  // TMDB平均評価

    @CreationTimestamp
    private LocalDateTime createdAt;  // 登録日時を自動生成

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 更新日時を自動更新

    @ElementCollection
    @CollectionTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id")
    )
    @Column(name = "genre")
    @Builder.Default
    private Set<String> genres = new HashSet<>();  // ジャンル一覧

    @ElementCollection
    @CollectionTable(
            name = "movie_original_country",
            joinColumns = @JoinColumn(name = "movie_id")
    )
    @Column(name = "country")
    @Builder.Default
    private Set<String> originalCountries = new HashSet<>();  // 制作国

    @ElementCollection
    @CollectionTable(
            name = "movie_cast",
            joinColumns = @JoinColumn(name = "movie_id")
    )
    @Builder.Default
    private List<MovieCast> casts = new ArrayList<>();  // キャスト情報
}
