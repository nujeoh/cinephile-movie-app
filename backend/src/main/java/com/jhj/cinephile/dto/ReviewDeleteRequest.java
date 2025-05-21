package com.jhj.cinephile.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewDeleteRequest {

    @NotNull(message = "userIdは必須です。")
    private String userId;

    @NotNull(message = "reviewIdは必須です。")
    private String reviewId;
}
