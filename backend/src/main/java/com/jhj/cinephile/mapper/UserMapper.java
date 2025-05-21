package com.jhj.cinephile.mapper;

import com.jhj.cinephile.dto.UserDetailResponse;
import com.jhj.cinephile.entity.UserEntity;
import com.jhj.cinephile.util.HashidsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final HashidsService hashidsService;

    public UserDetailResponse toDto(UserEntity entity) {
        return UserDetailResponse.builder()
                .id(hashidsService.encode(entity.getId()))
                .name(entity.getName())
                .email(entity.getEmail())
                .reviewCount(entity.getReviewCount())
                .likeCount(entity.getGivenLikeCount())
                .commentCount(entity.getCommentCount())
                .profileImageUrl(entity.getProfileImageUrl())
                .build();
    }
}
