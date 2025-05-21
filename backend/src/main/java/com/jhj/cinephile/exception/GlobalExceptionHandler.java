package com.jhj.cinephile.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
// 全体の例外処理を担当するクラス
public class GlobalExceptionHandler {

    // バリデーションエラーの処理
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    // メールアドレス重複時の処理
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("email", ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    // ユーザーが見つからない場合の処理
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    // 映画が見つからない場合の処理
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<?> handleMovieNotFound(MovieNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    // レビューが見つからない場合の処理
    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<?> handleReviewNotFound(ReviewNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    // 同じ映画に既にレビューが存在する場合の処理
    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<?> handleReviewAlreadyExists(ReviewAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }

    // 認証エラーの処理
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthException(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of("message", ex.getMessage()));
    }

    // 無効なHash IDの処理
    @ExceptionHandler(InvalidHashIdException.class)
    public ResponseEntity<?> handleInvalidHashId(InvalidHashIdException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", ex.getMessage()));
    }

    // ファイル保存時のエラー処理
    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<?> handleFileStorageException(FileStorageException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", ex.getMessage()));
    }
}