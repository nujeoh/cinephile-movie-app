package com.jhj.cinephile.jwt;

import com.jhj.cinephile.entity.UserEntity;
import com.jhj.cinephile.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

// JWT認証フィルター
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    /**
     * フィルタ処理のエントリポイント
     * リクエストごとにJWTを検証し、認証設定を行う
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = resolveToken(request);  // Authorizationヘッダーからトークンを抽出

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            Optional<UserEntity> optionalUser = userService.findById(userId);

            if (optionalUser.isPresent()) {
                UserEntity user = optionalUser.get();

                // 認証トークンを作成し、SecurityContextに設定
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);  // 次のフィルターへ処理を委譲
    }

    // リクエストヘッダーからBearerトークンを抽出
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        log.info("Authorization Header: {}", bearer);
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    /**
     * エラーレスポンスを設定
     * 認証失敗時にJSON形式でメッセージを返す
     */
    private void setErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
        PrintWriter writer = response.getWriter();
        writer.write("message: " + message);
        writer.flush();
    }
}
