package com.jhj.cinephile.repository;

import com.jhj.cinephile.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findByReviewId(Long reviewId, Pageable pageable);
}
