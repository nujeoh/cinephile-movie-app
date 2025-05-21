package com.jhj.cinephile.controller;

import com.jhj.cinephile.dto.*;
import com.jhj.cinephile.entity.UserEntity;
import com.jhj.cinephile.service.CommentService;
import com.jhj.cinephile.service.ReviewLikeService;
import com.jhj.cinephile.service.ReviewService;
import com.jhj.cinephile.util.HashidsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.List;

/**
 * レビュー関連のREST APIコントローラー
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewLikeService reviewLikeService;
    private final CommentService commentService;
    private final HashidsService hashidsService;

    /**
     * レビュー詳細取得
     * @param hashId ハッシュ化されたレビューID
     * @param user 認証ユーザー情報 (Optional)
     * @return 200 OK + レビュー詳細 or 404 Not Found
     */
    @GetMapping("/{hashId}")
    public ResponseEntity<ReviewResponse> getReviewDetail(
            @PathVariable String hashId,
            @AuthenticationPrincipal UserEntity user
    ) {
        Long reviewId = hashidsService.decode(hashId);
        return reviewService.getReviewDetail(reviewId, user)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * コメント一覧ページング取得
     * @param hashId ハッシュ化されたレビューID
     * @param pageable ページ情報 (size=5, createdAt降順)
     * @return コメントのページ一覧
     */
    @GetMapping("/{hashId}/comments")
    public ResponseEntity<Page<CommentResponse>> getComments(
            @PathVariable String hashId,
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        Long reviewId = hashidsService.decode(hashId);
        return ResponseEntity.ok(
                commentService.getComments(reviewId, pageable)
        );
    }

    /**
     * 上位3レビューのプレビュー取得 (映画別)
     * @param hashId ハッシュ化された映画ID
     * @param user 認証ユーザー情報 (Optional)
     * @return 上位3件のレビューリスト
     */
    @GetMapping("/movie/preview/{hashId}")
    public ResponseEntity<List<ReviewResponse>> getTop3ReviewsByMovie(
            @PathVariable String hashId,
            @AuthenticationPrincipal UserEntity user
    ) {
        Long movieId = hashidsService.decode(hashId);
        return ResponseEntity.ok(
                reviewService.getTop3ReviewsByMovie(movieId, user)
        );
    }

    /**
     * 映画別レビュー一覧ページング取得
     * @param hashId ハッシュ化された映画ID
     * @param sort ソート条件 ("recent"/"high"/"low"/"like")
     * @param user 認証ユーザー情報 (Optional)
     * @param pageable ページ情報 (size=5)
     * @return ソート済みレビューのページ一覧
     */
    @GetMapping("/movie/all/{hashId}")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByMoviePaged(
            @PathVariable String hashId,
            @RequestParam(defaultValue = "like") String sort,
            @AuthenticationPrincipal UserEntity user,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        Long movieId = hashidsService.decode(hashId);

        Sort sortObj = switch (sort) {
            case "recent" -> Sort.by("createdAt").descending();
            case "high" -> Sort.by("rating").descending();
            case "low" -> Sort.by("rating").ascending();
            default -> Sort.by("likeCount").descending();
        };
        Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortObj);
        return ResponseEntity.ok(
                reviewService.getReviewsByMovieId(movieId, user, sorted)
        );
    }

    /**
     * 上位3レビューのプレビュー取得 (ユーザー別)
     * @param user 認証ユーザー情報 (Optional)
     * @return 上位3件のレビューリスト
     */
    @GetMapping("/user/preview")
    public ResponseEntity<List<ReviewResponse>> getTop3ReviewsByUser(
            @AuthenticationPrincipal UserEntity user
    ) {
        return ResponseEntity.ok(reviewService.getTop3ReviewsByUser(user));
    }

    /**
     * ユーザー別レビュー一覧ページング取得
     * @param sort ソート条件 ("recent"/"high"/"low"/"like")
     * @param user 認証ユーザー情報 (Optional)
     * @param pageable ページ情報 (size=5)
     * @return ソート済みレビューのページ一覧
     */
    @GetMapping("/user/all")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByUserPaged(
            @RequestParam(defaultValue = "like") String sort,
            @AuthenticationPrincipal UserEntity user,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        Sort sortObj = switch (sort) {
            case "recent" -> Sort.by("createdAt").descending();
            case "high" -> Sort.by("rating").descending();
            case "low" -> Sort.by("rating").ascending();
            default -> Sort.by("likeCount").descending();
        };

        Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortObj);
        return ResponseEntity.ok(
                reviewService.getReviewsByUserId(user, sorted)
        );
    }

    /**
     * 新規レビュー登録
     * @param request レビュー作成リクエスト (バリデーション済み)
     * @param user 認証ユーザー情報
     * @return 201 Created + 作成レビュー情報
     */
    @PostMapping("/register")
    public ResponseEntity<ReviewResponse> registerReview(
            @Valid
            @RequestBody ReviewRegisterRequest request,
            @AuthenticationPrincipal UserEntity user
    ) {
        ReviewResponse response = reviewService.createReview(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * レビューのいいねトグル
     * @param hashId ハッシュ化されたレビューID
     * @param user 認証ユーザー情報
     * @return 201 Created (いいね) または 200 OK (取り消し)
     */
    @PostMapping("/{hashId}/like")
    public ResponseEntity<ReviewLikeResponse> toggleLike(
            @PathVariable String hashId,
            @AuthenticationPrincipal UserEntity user
    ) {
        Long reviewId = hashidsService.decode(hashId);
        ReviewLikeResponse response = reviewLikeService.toggleLike(reviewId, user.getId());
        return response.isLiked() ?
                ResponseEntity.status(HttpStatus.CREATED).body(response) :
                ResponseEntity.ok(response);
    }

    /**
     * レビューコメント登録
     * @param request コメント作成リクエスト (バリデーション済み)
     * @param user 認証ユーザー情報
     * @return 201 Created + コメント情報
     */
    @PostMapping("/comment")
    public ResponseEntity<CommentResponse> registerComment(
            @Valid
            @RequestBody CommentRegisterRequest request,
            @AuthenticationPrincipal UserEntity user
    ) {
        CommentResponse response = commentService.register(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * レビューを更新するAPI
     * @param request 更新リクエストデータ（レビューID、評価、内容など）
     * @param user 認証されたユーザー情報
     * @return 更新されたレビュー情報
     * @throws AuthenticationException 認証エラーが発生した場合
     */
    @PutMapping("/update")
    public ResponseEntity<ReviewResponse> updateReview(
            @Valid
            @RequestBody ReviewUpdateRequest request,
            @AuthenticationPrincipal UserEntity user
    ) throws AuthenticationException {
        ReviewResponse response = reviewService.update(request, user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * レビューを削除するAPI
     * @param request 削除リクエストデータ（レビューIDなど）
     * @param user 認証されたユーザー情報
     * @throws AuthenticationException 認証エラーが発生した場合
     */
    @DeleteMapping("/delete")
    public void deleteReview(
            @Valid
            @RequestBody ReviewDeleteRequest request,
            @AuthenticationPrincipal UserEntity user
    ) throws AuthenticationException {
        reviewService.delete(request, user);
    }
}