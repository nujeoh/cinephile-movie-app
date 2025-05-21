package com.jhj.cinephile.repository;

import com.jhj.cinephile.entity.ReviewLikeEntity;
import com.jhj.cinephile.entity.id.ReviewLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewLikeRepository extends JpaRepository<ReviewLikeEntity, Long> {

    void deleteById(ReviewLikeId likeId);

    long countByReviewId(Long reviewId);

    boolean existsById(ReviewLikeId likeId);

    @Query("""
        select l.review.id
        from ReviewLikeEntity l
        where l.user.id = :userId
        and l.review.id in :reviewIds
     """)
    List<Long> findLikedReviewIds(
            @Param("userId")    Long userId,
            @Param("reviewIds") List<Long> reviewIds
    );

    boolean existsByReviewIdAndUserId(Long reviewId, Long userId);
}
