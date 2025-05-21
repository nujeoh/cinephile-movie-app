package com.jhj.cinephile.config;

import org.hashids.Hashids;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HashidsConfig {

    @Bean
    public Hashids hashids() {
        return new Hashids("cinephile_app_salt", 8);
    }
}