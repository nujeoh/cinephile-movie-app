package com.jhj.cinephile.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommnetUpdateRequest {

    @NotNull(message = "userIdは必須です。")
    private String userId;

    @NotNull(message = "commentIdは必須です。")
    private String commentId;

    @Size(min = 1, max = 1000, message = "レビュー内容は1000文字以内で入力してください。")
    private String content;
}
