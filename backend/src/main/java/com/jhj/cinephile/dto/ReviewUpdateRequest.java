package com.jhj.cinephile.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewUpdateRequest {

    @NotNull(message = "userIdは必須です。")
    private String userId;

    @NotNull(message = "reviewIdは必須です。")
    private String reviewId;

    @NotNull(message = "評価は必須です。")
    @DecimalMin(value = "0.5", message = "評価は0.5以上でなければなりません。")
    @DecimalMax(value = "5.0", message = "評価は5.0以下でなければなりません。")
    private Double rating;

    @Size(min = 1, max = 1000, message = "レビュー内容は1000文字以内で入力してください。")
    private String content;
}
