package com.jhj.cinephile.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CastResponse {
    private String name;
    private String character;
    private String profilePath;
}