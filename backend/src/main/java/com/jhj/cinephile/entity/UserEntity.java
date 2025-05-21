package com.jhj.cinephile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * ユーザー情報を表すエンティティクラス
 * - Spring SecurityのUserDetailsを実装して認証・認可で使用
 */
@Entity
@Table(name = "users")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ユーザー固有ID

    private String name;  // ユーザー名

    @Column(unique = true)
    private String email;  // ログイン用メールアドレス（ユニーク）

    @JsonIgnore
    private String password;  // ハッシュ化されたパスワード

    @Formula("(select count(*) from reviews r where r.user_id = id)")
    private int reviewCount;  // 投稿したレビュー数

    @Formula("(select count(*) from review_comments rc where rc.user_id = id)")
    private int commentCount;  // 投稿したコメント数

    @Formula("(select count(*) from review_likes rl where rl.user_id = id)")
    private int givenLikeCount;  // 受け取ったいいね数

    @Column(length = 512)
    private String profileImageUrl;  // プロフィール画像URL

    @Column(length = 256)
    private String profileImageKey;  // S3に保存された画像キー

    /**
     * プロフィール画像情報の更新
     * @param newUrl 新しい画像URL
     * @param newKey 新しい画像キー
     */
    public void updateProfileImage(String newUrl, String newKey) {
        if (newUrl == null || newKey == null) {
            throw new IllegalArgumentException("画像URLとキーはnullにできません。");
        }
        this.profileImageUrl = newUrl;
        this.profileImageKey = newKey;
    }

    /**
     * ユーザーの権限一覧を返却
     * ROLE_USERを単一で付与
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_USER");
    }

    /**
     * 認証時に使用するユーザー名を返却
     * emailを使用
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    // 以下、アカウントステータス関連のフラグを常にtrueに設定
    @Override
    public boolean isAccountNonExpired() {
        return true;  // アカウント有効期限切れでないか
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // アカウントがロックされていないか
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 資格情報（パスワード）の有効期限切れでないか
    }

    @Override
    public boolean isEnabled() {
        return true;  // アカウントが有効かどうか
    }
}
