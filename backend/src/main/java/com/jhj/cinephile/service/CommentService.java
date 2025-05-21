package com.jhj.cinephile.service;

import com.jhj.cinephile.dto.CommentRegisterRequest;
import com.jhj.cinephile.dto.CommentResponse;
import com.jhj.cinephile.entity.CommentEntity;
import com.jhj.cinephile.entity.ReviewEntity;
import com.jhj.cinephile.entity.UserEntity;
import com.jhj.cinephile.exception.ReviewNotFoundException;
import com.jhj.cinephile.mapper.CommentMapper;
import com.jhj.cinephile.repository.CommentRepository;
import com.jhj.cinephile.repository.ReviewRepository;
import com.jhj.cinephile.util.HashidsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// レビューコメント管理サービス
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository reviewCommentRepository;
    private final ReviewRepository reviewRepository;
    private final HashidsService hashidsService;
    private final CommentMapper mapper;

    /**
     * レビューにコメント登録
     * レビューが存在しない場合は例外
     */
    @Transactional
    public CommentResponse register(CommentRegisterRequest request, UserEntity user) {
        Long reviewId = hashidsService.decode(request.getReviewId());

        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(
                        () -> new ReviewNotFoundException("レビューが見つかりません。")
                );

        CommentEntity reviewCommentEntity = CommentEntity.builder()
                .review(review)
                .user(user)
                .content(request.getContent())
                .build();

        CommentEntity saved = reviewCommentRepository.save(reviewCommentEntity);

        return mapper.toDto(saved);  // DTO変換後返却
    }

    // レビューのコメントをページング取得
    public Page<CommentResponse> getComments(Long reviewId, Pageable pageable) {
        return reviewCommentRepository.findByReviewId(reviewId, pageable)
                .map(mapper::toDto);  // 各エンティティをDTOにマッピング
    }
}
