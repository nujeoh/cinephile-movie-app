package com.jhj.cinephile.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

// バッチ手動実行用RESTコントローラー
@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
public class BatchJobController {

    private final JobLauncher jobLauncher;
    private final Job discoverJob;

    /**
     * Discoverバッチ強制実行エンドポイント
     * 200 OK: 開始完了メッセージ
     * 500 Internal Server Error: エラーメッセージ
     */
    @PostMapping("/discover")
    public ResponseEntity<String> runDiscover(
            @RequestParam("from") String from,
            @RequestParam("to")   String to
    ) {
        try {
            var params = new JobParametersBuilder()
                    .addString("from", from)
                    .addString("to",   to)
                    .addLong("run.id", new Date().getTime())
                    .toJobParameters();

            jobLauncher.run(discoverJob, params);
            return ResponseEntity.ok(
                    "Discoverバッチを開始しました: from=" + from + ", to=" + to
            );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Discoverバッチを開始しました: " + e.getMessage());
        }
    }
}