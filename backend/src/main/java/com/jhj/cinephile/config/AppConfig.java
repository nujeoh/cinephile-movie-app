package com.jhj.cinephile.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// アプリケーション全体で使用するBean設定クラス
@Configuration
public class AppConfig {

    // パスワードをBCryptアルゴリズムで暗号化/検証するPasswordEncoderを登録
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
