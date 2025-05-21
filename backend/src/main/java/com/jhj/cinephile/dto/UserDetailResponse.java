package com.jhj.cinephile.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDetailResponse {

    private String id;
    private String email;
    private String name;
    private String profileImageUrl;
    private int reviewCount;
    private int commentCount;
    private int likeCount;
}
