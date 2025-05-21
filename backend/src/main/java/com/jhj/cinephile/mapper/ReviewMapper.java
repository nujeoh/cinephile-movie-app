package com.jhj.cinephile.mapper;

import com.jhj.cinephile.dto.ReviewResponse;
import com.jhj.cinephile.entity.ReviewEntity;
import com.jhj.cinephile.entity.UserEntity;
import com.jhj.cinephile.util.HashidsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final HashidsService hashidsService;

    public ReviewResponse toDto(ReviewEntity review, boolean liked) {
        return ReviewResponse.builder()
                .id(hashidsService.encode(review.getId()))
                .movieId(hashidsService.encode(review.getMovie().getId()))
                .userId(hashidsService.encode(review.getUser().getId()))
                .userName(review.getUser().getName())
                .profileImageUrl(review.getUser().getProfileImageUrl())
                .rating(review.getRating())
                .content(review.getContent())
                .liked(liked)
                .likeCount(review.getLikeCount())
                .commentCount(review.getCommentCount())
                .createdAt(review.getCreatedAt())
                .build();
    }
}