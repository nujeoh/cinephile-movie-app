package com.jhj.cinephile.service;

import com.jhj.cinephile.dto.*;
import com.jhj.cinephile.entity.CommentEntity;
import com.jhj.cinephile.entity.MovieEntity;
import com.jhj.cinephile.entity.ReviewEntity;
import com.jhj.cinephile.entity.UserEntity;
import com.jhj.cinephile.exception.MovieNotFoundException;
import com.jhj.cinephile.exception.ReviewAlreadyExistsException;
import com.jhj.cinephile.exception.ReviewNotFoundException;
import com.jhj.cinephile.mapper.ReviewMapper;
import com.jhj.cinephile.repository.MovieRepository;
import com.jhj.cinephile.repository.ReviewLikeRepository;
import com.jhj.cinephile.repository.ReviewRepository;
import com.jhj.cinephile.util.HashidsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// レビュー管理サービス
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final MovieRepository movieRepository;
    private final HashidsService hashidsService;
    private final ReviewMapper mapper;


    // 新しいレビューを作成
    @Transactional
    public ReviewResponse createReview(UserEntity user, ReviewRegisterRequest request) {
        Long movieId = hashidsService.decode(request.getMovieId());

        // 映画存在チェック
        MovieEntity movieEntity = movieRepository.findById(movieId).orElseThrow(
                () -> new MovieNotFoundException("映画が見つかりません。")
        );

        // 同一ユーザーの重複レビュー防止
        if (reviewRepository.existsByMovie_IdAndUser_Id(movieId, user.getId())) {
            throw new ReviewAlreadyExistsException("この映画にはすでにレビューを投稿済みです。");
        }

        // エンティティ組み立て
        ReviewEntity review = ReviewEntity.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .movie(movieEntity)
                .user(user)
                .build();

        // DB保存
        ReviewEntity saved = reviewRepository.save(review);
        return mapper.toDto(saved, false);
    }

    // レビュー詳細取得
    @Transactional(readOnly = true)
    public Optional<ReviewResponse> getReviewDetail(Long reviewId, UserEntity user) {
        return reviewRepository.findById(reviewId)
                .map(review -> {
                    boolean liked = false;
                    if (user != null) {
                        liked = reviewLikeRepository
                                .existsByReviewIdAndUserId(review.getId(), user.getId());
                    }

                    return mapper.toDto(review, liked);
                });
    }

    // 映画毎のレビューをページ単位で取得
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByMovieId(
            Long movieId,
            UserEntity user,
            Pageable pageable
    ) {
        Page<ReviewEntity> page = reviewRepository.findByMovie_Id(movieId, pageable);

        List<Long> reviewIds = page.stream()
                .map(ReviewEntity::getId)
                .toList();

        Long userId = user != null ? user.getId() : -1L;
        Set<Long> likedReviewIds = new HashSet<>(
                reviewLikeRepository.findLikedReviewIds(userId, reviewIds)
        );

        return page.map(review -> {
            boolean liked = likedReviewIds.contains(review.getId());
            return mapper.toDto(review, liked);
        });
    }

    // ユーザー毎のレビューをページ単位で取得
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByUserId(UserEntity user, Pageable pageable) {
        Long userId = user != null ? user.getId() : -1L;

        Page<ReviewEntity> page = reviewRepository.findByUser_Id(userId, pageable);

        List<Long> reviewsIds = page.stream()
                .map(ReviewEntity::getId)
                .toList();

        Set<Long> likedReviewIds = new HashSet<>(
                reviewLikeRepository.findLikedReviewIds(userId, reviewsIds)
        );

        return page.map(review -> {
            boolean liked = likedReviewIds.contains(review.getId());
            return mapper.toDto(review, liked);
        });
    }

    // 映画の上位3レビューをプレビュー
    @Transactional(readOnly = true)
    public List<ReviewResponse> getTop3ReviewsByMovie(Long movieId, UserEntity user) {
        Pageable pageable = PageRequest.of(0, 3);

        List<ReviewEntity> reviews = reviewRepository
                .findTop3ByMovie_IdOrderByLikeCountDesc(movieId, pageable);

        List<Long> reviewsIds = reviews.stream()
                .map(ReviewEntity::getId)
                .toList();

        Long userId = user != null ? user.getId() : -1L;
        Set<Long> likedReviewIds = new HashSet<>(
                reviewLikeRepository.findLikedReviewIds(userId, reviewsIds)
        );

        return reviews.stream().map(review -> {
            boolean liked = likedReviewIds.contains(review.getId());
            return mapper.toDto(review, liked);
        }).toList();
    }

    // ユーザーの上位3レビューをプレビュー
    @Transactional(readOnly = true)
    public List<ReviewResponse> getTop3ReviewsByUser( UserEntity user) {
        Pageable pageable = PageRequest.of(0,3);

        Long userId = user != null ? user.getId() : -1L;

        List<ReviewEntity> reviews = reviewRepository
                .findTop3ByUser_IdOrderByCreatedAtDesc(userId, pageable);

        List<Long> reviewsIds = reviews.stream()
                .map(ReviewEntity::getId)
                .toList();

        Set<Long> likedReviewIds = new HashSet<>(
                reviewLikeRepository.findLikedReviewIds(userId, reviewsIds)
        );

        return reviews.stream().map(review -> {
            boolean liked = likedReviewIds.contains(review.getId());
            return mapper.toDto(review, liked);
        }).toList();
    }

    // レビューを編集
    @Transactional
    public ReviewResponse update(ReviewUpdateRequest request, UserEntity user) throws AuthenticationException {
        Long userId = hashidsService.decode(request.getUserId());
        Long currentId = user.getId();
        Long reviewId = hashidsService.decode(request.getReviewId());

        if (!userId.equals(currentId)) {
            throw new AuthenticationException("認証されたユーザーのみがレビューを更新できます。");
        }

        ReviewEntity review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewNotFoundException("レビューが見つかりません。")
        );

        review.updateReview(request.getRating(), request.getContent());

        ReviewEntity updated = reviewRepository.save(review);

        return mapper.toDto(updated, false);
    }

    // レビューを削除
    @Transactional
    public void delete(ReviewDeleteRequest request, UserEntity user) throws AuthenticationException {
        Long userId = hashidsService.decode(request.getUserId());
        Long currentId = user.getId();
        Long reviewId = hashidsService.decode(request.getReviewId());

        if (!userId.equals(currentId)) {
            throw new AuthenticationException("認証されたユーザーのみがレビューを削除できます。");
        }

        ReviewEntity review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewNotFoundException("レビューが見つかりません。")
        );

        reviewRepository.delete(review);
    }
}
