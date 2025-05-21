package com.jhj.cinephile.jwt;

import com.jhj.cinephile.entity.UserEntity;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

// JWTトークン生成および検証コンポーネント
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {


    // 秘密鍵
    @Value("${jwt.secret_key}")
    private String secretKey;

    // 有効期限（ミリ秒）
    @Value("${jwt.expiration_time}")
    private long expirationTime;

    /**
     * JWTトークンを生成
     * @return 生成されたJWTトークン
     */
    public String generateToken(UserEntity userEntity) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(userEntity.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // トークンからユーザーIDを取得
    public Long getUserIdFromToken(String token) {
        String subject = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Long.valueOf(subject);
    }

    /**
     * トークンの有効性を検証
     * @return 有効ならtrue、無効ならfalse
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("JWT malformed: {}", e.getMessage());
        } catch (SignatureException e) {
            log.warn("JWT invalid signature: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT illegal argument: {}", e.getMessage());
        }
        return false;
    }
}
