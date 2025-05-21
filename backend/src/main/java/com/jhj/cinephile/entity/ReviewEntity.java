package com.jhj.cinephile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 리뷰 엔티티
 * レビューエンティティ
 */
@Entity
@Table(name = "reviews")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // レビュー固有ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;  // 作成者

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;  // 対象映画

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLikeEntity> likes = new ArrayList<>();  // いいねリスト

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>();

    @Formula("(select count(*) from review_likes rl where rl.review_id = id)")
    private int likeCount;  // いいね数

    @Formula("(select count(*) from review_comments rc where rc.review_id = id)")
    private int commentCount;  // コメント数

    @Column(length = 1000, nullable = false)
    private String content;  // レビュー内容

    @Column(nullable = false)
    private Double rating;  // レーティング (例: 0.5~5.0)

    @CreationTimestamp
    private LocalDateTime createdAt;  // 作成日時を自動生成

    @UpdateTimestamp LocalDateTime updatedAt; // 編集日時を自動生成

    public void updateReview(Double rating, String content) {
        this.rating = rating;
        this.content = content;
    }
}
