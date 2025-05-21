package com.jhj.cinephile.http;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

// TMDB API呼び出し専用HTTPクライアント
@AllArgsConstructor
@Component
public class TmdbHttpClient {

    private final RestClient restClient;

    /**
     * 指定したURLへGETリクエストを送信
     * Content-Type: application/jsonヘッダーを追加
     * ResponseEntityで返却
     *
     * @param targetUrl 呼び出し対象のURLパス
     * @param responseType レスポンスボディの型
     */
    public <T> ResponseEntity<T> get(
            String targetUrl,
            Class<T> responseType
    ) {
        return restClient.get()
                .uri(targetUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .toEntity(responseType);
    }
}
