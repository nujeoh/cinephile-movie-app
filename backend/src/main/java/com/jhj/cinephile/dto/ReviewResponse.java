package com.jhj.cinephile.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReviewResponse {

    private String id;
    private String content;
    private double rating;
    private String userName;
    private String userId;
    private String profileImageUrl;
    private String movieId;
    private int likeCount;
    private int commentCount;
    private boolean liked;
    private LocalDateTime createdAt;
}
