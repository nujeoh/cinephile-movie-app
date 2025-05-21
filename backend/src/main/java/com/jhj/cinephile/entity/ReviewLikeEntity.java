package com.jhj.cinephile.entity;

import com.jhj.cinephile.entity.id.ReviewLikeId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_likes")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewLikeEntity {

    @EmbeddedId
    private ReviewLikeId id;  // 複合キー: レビューID + ユーザーID

    @MapsId("reviewId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewEntity review;  // 対象レビュー

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;  // いいねしたユーザー

    @CreationTimestamp
    private LocalDateTime createdAt;  // 作成日時を自動生成
}