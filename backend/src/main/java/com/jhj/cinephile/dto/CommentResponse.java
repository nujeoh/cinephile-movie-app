package com.jhj.cinephile.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CommentResponse {

    private String id;
    private String content;
    private String userName;
    private String profileImageUrl;
    private String reviewId;
    private String userId;
    private LocalDateTime createdAt;
}
