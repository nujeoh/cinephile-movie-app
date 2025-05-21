package com.jhj.cinephile.batch;

import com.jhj.cinephile.entity.MovieEntity;
import com.jhj.cinephile.entity.embeddable.MovieCast;
import com.jhj.cinephile.http.TmdbHttpClient;
import com.jhj.cinephile.http.TmdbMovieDto;
import com.jhj.cinephile.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;


// TMDB Discover APIを使用したバッチ設定
@Configuration
@EnableBatchProcessing  // Spring Batchを有効化
@RequiredArgsConstructor
public class DiscoverBatchConfig {

    private final JobRepository jobRepository;                 // バッチ Jobリポジトリ
    private final PlatformTransactionManager txManager;       // トランザクションマネージャー
    private final TmdbHttpClient tmdbClient;
    private final MovieRepository movieRepository;

    public record MovieChange(Long id) {}  // 処理対象の映画ID

    /**
     * DiscoverItemReader Bean定義
     * パラメータ: from〜toの期間
     */
    @Bean
    @StepScope  // jobParameters使用可能
    public ItemReader<MovieChange> discoverReader(
            @Value("#{jobParameters['from']}") String from,
            @Value("#{jobParameters['to']}")   String to
    ) {
        return new DiscoverItemReader(tmdbClient, from, to);
    }

    /**
     * Detail取得用Processor
     * 重複処理のスキップ
     */
    @Bean
    @StepScope
    public ItemProcessor<MovieChange, MovieEntity> detailProcessor(
            @Value("#{jobParameters['from']}") String from
    ) {
        LocalDateTime windowStart = LocalDate.parse(from).atStartOfDay();
        return change -> {
            var existing = movieRepository.findById(change.id());
            if (existing.isPresent()) {
                var updated = existing.get().getUpdatedAt();
                if (updated != null && updated.isAfter(windowStart)) {
                    return null;  // 既に処理済み
                }
            }

            String uri = "/movie/" + change.id()
                    + "?append_to_response=credits,release_dates&language=ja-JP";
            TmdbMovieDto detail = tmdbClient.get(uri, TmdbMovieDto.class).getBody();;

            if (detail == null) return null;

            String director = detail.getCredits().getCrew().stream()
                    .filter(c -> "Directing".equals(c.getDepartment()))
                    .map(TmdbMovieDto.CrewDto::getName)
                    .findFirst().orElse(null);

            String rating = detail.getReleaseDates().getResults().stream()
                    .filter(r -> "JP".equals(r.getCountryCode()))
                    .flatMap(r -> r.getReleaseDates().stream())
                    .map(TmdbMovieDto.ReleaseDateInfo::getCertification)
                    .filter(cert -> !cert.isEmpty())
                    .findFirst().orElse(null);

            return MovieEntity.builder()
                    .id(detail.getId())
                    .title(detail.getTitle())
                    .overview(detail.getOverview())
                    .releaseDate(detail.getReleaseDate())
                    .runtime(detail.getRuntime())
                    .backdropPath(detail.getBackdropPath())
                    .posterPath(detail.getPosterPath())
                    .director(director)
                    .filmRating(rating)
                    .genres(detail.getGenres().stream()
                            .map(g -> g.getName()).collect(Collectors.toSet()))
                    .originalCountries(new HashSet<>(detail.getOriginCountry()))
                    .casts(detail.getCredits().getCast().stream()
                            .map(c -> MovieCast.builder()
                                    .name(c.getName())
                                    .character(c.getCharacter())
                                    .profilePath(c.getProfilePath()).build())
                            .collect(Collectors.toList()))
                    .build();
        };
    }

    /**
     * Writer
     * DB保存
     */
    @Bean
    public ItemWriter<MovieEntity> writer() {
        return items -> movieRepository.saveAll(items);
    }

    /**
     * Step定義: chunk処理
     * skip, retryポリシー
     */
    @Bean
    public Step discoverStep(
            ItemReader<MovieChange> discoverReader,
            ItemProcessor<MovieChange, MovieEntity> detailProcessor,
            ItemWriter<MovieEntity> writer
    ) {
        return new StepBuilder("discoverStep", jobRepository)
                .<MovieChange, MovieEntity>chunk(20, txManager)
                .reader(discoverReader)
                .processor(detailProcessor)
                .writer(writer)
                .faultTolerant()
                .retry(HttpClientErrorException.TooManyRequests.class)
                .retryLimit(3)
                .retry(ResourceAccessException.class)
                .retryLimit(3)
                .skip(RestClientException.class)
                .skipLimit(1000)
                .build();
    }

    /**
     * Job定義
     * RunIdIncrementerで実行ごとにユニークID付与
     */
    @Bean
    public Job discoverJob(Step discoverStep) {
        return new JobBuilder("discoverJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(discoverStep)
                .build();
    }
}
