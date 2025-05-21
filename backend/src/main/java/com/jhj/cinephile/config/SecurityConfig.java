package com.jhj.cinephile.config;

import com.jhj.cinephile.jwt.JwtAuthenticationFilter;
import com.jhj.cinephile.jwt.JwtTokenProvider;
import com.jhj.cinephile.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;



// Spring Security設定クラス
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    // JWT認証フィルタBean登録
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, userService);
    }

    // SecurityFilterChain設定
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/review/register",
                                "/api/review/*/like")
                        .authenticated() // 認証必要
                        .anyRequest().permitAll() // その他のリクエストは許可
                );

        // JWT認証フィルタをUsernamePasswordAuthenticationFilterの前に配置
        http.addFilterBefore(
                jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    // CORS設定ソース
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:3000",
                "http://cinephile-app.com",
                "http://cinephile-app.com.s3-website-ap-northeast-1.amazonaws.com")); // 許可ドメイン
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS")); // 許可HTTPメソッド
        config.setAllowedHeaders(List.of("*")); // すべてのヘッダを許可
        config.setAllowCredentials(true); // 認証情報の使用を許可

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // すべてのパスに適用
        return source;
    }
}
