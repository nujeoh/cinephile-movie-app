package com.jhj.cinephile.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class CommentRegisterRequest {

    @NotNull(message = "reviewIdは必須です。")
    private String reviewId;

    @Size(min = 1, max = 1000, message = "レビュー内容は1000文字以内で入力してください。")
    private String content;
}
