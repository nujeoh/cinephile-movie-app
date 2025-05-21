package com.jhj.cinephile.controller;

import com.jhj.cinephile.dto.MovieResponse;
import com.jhj.cinephile.service.MovieService;
import com.jhj.cinephile.util.HashidsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 映画取得用のREST APIコントローラー
 * - HashidsでエンコードされたIDをデコードして映画情報を取得・生成
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie")
public class MovieController {

    private final MovieService movieService;
    private final HashidsService hashidsService;

    /**
     * 映画詳細取得
     * @param hashId Hashidsでエンコードされた映画ID
     * @return 200 OK + 映画詳細情報を返却
     */
    @GetMapping("/{hashId}")
    public ResponseEntity<MovieResponse> getMovieDetail(
            @PathVariable String hashId
    ) {
        Long movieId = hashidsService.decode(hashId);
        MovieResponse res = movieService.getOrCreateMovie(movieId);
        return ResponseEntity.ok(res);
    }
}
