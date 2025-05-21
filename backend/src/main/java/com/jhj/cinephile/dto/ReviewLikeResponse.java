package com.jhj.cinephile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewLikeResponse {

    private boolean liked;
    private long likeCount;
}
