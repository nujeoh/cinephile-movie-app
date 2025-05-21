package com.jhj.cinephile.batch;

import com.jhj.cinephile.batch.DiscoverBatchConfig.MovieChange;
import com.jhj.cinephile.http.TmdbHttpClient;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// TMDB Discover APIをベースにしたItemReader
public class DiscoverItemReader implements ItemReader<MovieChange> {

    private final TmdbHttpClient client;
    private final LocalDate fromDate, toDate;

    // ページ管理
    private int page = 1;
    private int totalPages = Integer.MAX_VALUE;

    // 現在ページのバッファとインデックス
    private List<MovieChange> buffer;
    private int idx = 0;

    // コンストラクタ: 期間パラメータをパース
    public DiscoverItemReader(TmdbHttpClient client, String from, String to) {
        this.client   = client;
        this.fromDate = LocalDate.parse(from);
        this.toDate   = LocalDate.parse(to);
    }

    /**
     * 1件ずつMovieChangeを返却
     * バッファ枯渇時に次ページを取得
     * もうページがなければnull
     */
    @Override
    public MovieChange read() {
        if (buffer == null || idx >= buffer.size()) {
            if (page > totalPages) {
                return null;  // 全て読み込み完了
            }

            // Discover API URI 빌드 // Discover APIのURIを構築
            String uri = UriComponentsBuilder
                    .fromPath("/discover/movie")
                    .queryParam("include_adult",  false)
                    .queryParam("include_video",  false)
                    .queryParam("language",       "ja-JP")
                    .queryParam("region",         "JP")
                    .queryParam("release_date.gte", fromDate)
                    .queryParam("release_date.lte", toDate)
                    .queryParam("sort_by",        "popularity.desc")
                    .queryParam("page",          page++)
                    .toUriString();

            // API呼び出しとレスポンスバインディング
            ResponseEntity<DiscoverResponse> response = client.get(uri, DiscoverResponse.class);
            DiscoverResponse body = response.getBody();
            if (body == null || body.getResults().isEmpty()) {
                return null;
            }

            totalPages = body.getTotalPages();  // 総ページ数を設定
            buffer = body.getResults().stream()
                    .map(r -> new MovieChange(r.getId()))
                    .collect(Collectors.toList());
            idx = 0;  // バッファリセット
        }

        return buffer.get(idx++);  // 次の項目を返却
    }


    // Discover APIレスポンス用DTO
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DiscoverResponse {
        private int page;
        private int totalResults;
        private int totalPages;
        private List<Result> results;

        @Data
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Result {
            private long id;  // 映画ID
        }
    }
}