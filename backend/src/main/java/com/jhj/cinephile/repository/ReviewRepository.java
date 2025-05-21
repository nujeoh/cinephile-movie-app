package com.jhj.cinephile.repository;

import com.jhj.cinephile.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Optional<ReviewEntity> findById(Long reviewId);

    List<ReviewEntity> findTop3ByMovie_IdOrderByLikeCountDesc(Long movieId, Pageable pageable);

    Page<ReviewEntity> findByMovie_Id(Long movieId, Pageable pageable);

    boolean existsByMovie_IdAndUser_Id(Long movieId, Long userId);

    List<ReviewEntity> findTop3ByUser_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<ReviewEntity> findByUser_Id(Long userId, Pageable pageable);




}