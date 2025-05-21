package com.jhj.cinephile.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

// TMDB APIを呼び出すRestClient設定クラス
@Configuration
public class RestClientConfig {

    @Value("${tmdb.api_key}")
    private String apiKey; // // TMDB API KEY

    // RestClient.Builder Beanを登録
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    /**
     * RestClient Beanを登録
     * 基本URL、ヘッダー、ステータスコードごとの例外処理を設定
     * @param restClientBuilder RestClientビルダー
     * @return 設定済みRestClientインスタンス
     */
    @Bean
    public RestClient restClient(RestClient.Builder restClientBuilder) {
        return restClientBuilder
                // 基本URL設定: TMDB APIエンドポイント
                .baseUrl("https://api.themoviedb.org/3")
                // JSONレスポンスAcceptヘッダー
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                // 認証ヘッダー: Bearer + API KEY
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                // 4xxクライアントエラー処理: RestClientExceptionを投げる
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new RestClientException("クライアントエラー: " + res.getStatusCode());
                })
                // 5xxサーバーエラー処理: RestClientExceptionを投げる
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, (req, res) -> {
                    throw new RestClientException("サーバーエラー: " + res.getStatusCode());
                })
                .build();
    }
}
