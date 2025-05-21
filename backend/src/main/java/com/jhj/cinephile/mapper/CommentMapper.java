package com.jhj.cinephile.mapper;

import com.jhj.cinephile.dto.CommentResponse;
import com.jhj.cinephile.entity.CommentEntity;
import com.jhj.cinephile.util.HashidsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final HashidsService hashidsService;

    public CommentResponse toDto(CommentEntity reviewComment) {
        return CommentResponse.builder()
                .id(hashidsService.encode(reviewComment.getId()))
                .content(reviewComment.getContent())
                .userName(reviewComment.getUser().getName())
                .profileImageUrl(reviewComment.getUser().getProfileImageUrl())
                .userId(hashidsService.encode(reviewComment.getUser().getId()))
                .reviewId(hashidsService.encode(reviewComment.getReview().getId()))
                .createdAt(reviewComment.getCreatedAt())
                .build();
    }
}
