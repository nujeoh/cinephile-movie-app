package com.jhj.cinephile.entity.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

// レビューLikeエンティティ用複合キー
@Embeddable
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewLikeId implements Serializable {

    private Long reviewId;
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewLikeId that)) return false;
        return Objects.equals(reviewId, that.reviewId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId, userId);
    }
}
