package com.jhj.cinephile.service;

import com.jhj.cinephile.dto.ReviewLikeResponse;
import com.jhj.cinephile.entity.ReviewEntity;
import com.jhj.cinephile.entity.ReviewLikeEntity;
import com.jhj.cinephile.entity.UserEntity;
import com.jhj.cinephile.entity.id.ReviewLikeId;
import com.jhj.cinephile.exception.ReviewNotFoundException;
import com.jhj.cinephile.exception.UserNotFoundException;
import com.jhj.cinephile.repository.ReviewLikeRepository;
import com.jhj.cinephile.repository.ReviewRepository;
import com.jhj.cinephile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// レビューいいねトグルサービス
@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;

    /**
     * レビューいいね状態をトグル
     * レビューまたはユーザーが存在しない場合は例外
     * すでにいいねあれば削除、なければ作成
     * 合計いいね数を計算し応答
     */
    @Transactional
    public ReviewLikeResponse toggleLike(Long reviewId, Long userId) {

        // レビュー存在チェック
        ReviewEntity reviewEntity = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewNotFoundException("レビューが見つかりません。")
        );

        // ユーザー存在チェック
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("ユーザーが見つかりません。")
        );

        ReviewLikeId likeId = new ReviewLikeId(reviewId, userId);  // 複合キー生成

        boolean liked;

        // 既にいいねオブジェクトがあれば削除、なければ追加
        if (reviewLikeRepository.existsById(likeId)) {
            reviewLikeRepository.deleteById(likeId);
            liked = false;  // いいねキャンセル
        } else {
            ReviewLikeEntity like = ReviewLikeEntity.builder()
                    .id(likeId)
                    .review(reviewEntity)
                    .user(userEntity)
                    .build();
            reviewLikeRepository.save(like);
            liked = true;  // いいね完了
        }

        long likeCount = reviewLikeRepository.countByReviewId(reviewId);  // 総いいね数

        return new ReviewLikeResponse(liked, likeCount);  // 結果DTO返却
    }
}
