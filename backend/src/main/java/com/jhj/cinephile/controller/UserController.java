package com.jhj.cinephile.controller;

import com.jhj.cinephile.dto.UserDetailResponse;
import com.jhj.cinephile.dto.UserLoginRequest;
import com.jhj.cinephile.dto.UserLoginResponse;
import com.jhj.cinephile.dto.UserSignupRequest;
import com.jhj.cinephile.entity.UserEntity;
import com.jhj.cinephile.jwt.JwtTokenProvider;
import com.jhj.cinephile.mapper.UserMapper;
import com.jhj.cinephile.service.UserService;
import com.jhj.cinephile.util.HashidsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.Map;

/**
 * ユーザー関連のREST APIコントローラー
 * - 会員登録、ログイン、ユーザー詳細取得、プロフィール画像更新を提供
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final HashidsService hashidsService;
    private final UserMapper userMapper;

    /**
     * 会員登録エンドポイント
     * @param request ユーザー登録リクエスト (バリデーション済み)
     * @return 201 Created
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> register(
            @Valid @RequestBody UserSignupRequest request
    ) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * ログインエンドポイント
     * @param request ユーザー認証情報 (バリデーション済み)
     * @return 200 OK + JWTトークン
     */
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(
            @Valid @RequestBody UserLoginRequest request
    ) {
        UserEntity authenticated = userService.authenticateUser(request);
        String token = jwtTokenProvider.generateToken(authenticated);
        return ResponseEntity.ok(new UserLoginResponse(token));
    }

    /**
     * ユーザー詳細取得エンドポイント
     * @param user 認証ユーザー情報
     * @return 200 OK + ユーザー詳細情報
     */
    @GetMapping
    public ResponseEntity<UserDetailResponse> getUserDetail(
            @AuthenticationPrincipal UserEntity user
    ) {
        // DTO変換後にレスポンス
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    /**
     * プロフィール画像更新エンドポイント
     * @param hashId HashidsでエンコードされたユーザーID
     * @param user 認証ユーザー情報
     * @param file アップロードされたプロフィール画像ファイル
     * @return 200 OK + 新規プロフィール画像のURL
     * @throws IOException ファイル処理エラー時にスロー
     * @throws AuthenticationException 他ユーザーの更新試行時にスロー
     */
    @PutMapping("/{hashId}/profile-image")
    public ResponseEntity<Map<String, String>> updateProfileImage(
            @PathVariable String hashId,
            @AuthenticationPrincipal UserEntity user,
            @RequestParam("file") MultipartFile file
    ) throws IOException, AuthenticationException {
        // ハッシュIDをデコード
        Long decodedId = hashidsService.decode(hashId);
        // 自身のIDチェック
        if (!user.getId().equals(decodedId)) {
            throw new AuthenticationException("自身のプロフィールのみ更新可能です。");
        }
        // プロフィール画像の更新処理
        String newUrl = userService.updateProfileImage(user, file);
        return ResponseEntity.ok(Map.of("url", newUrl));
    }
}
