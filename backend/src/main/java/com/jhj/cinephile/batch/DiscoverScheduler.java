package com.jhj.cinephile.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


// デイリーディスカバリバッチスケジューラー
@Component
@EnableScheduling
@RequiredArgsConstructor
public class DiscoverScheduler {

    private final JobLauncher jobLauncher;
    private final Job discoverJob;

    /**
     * 毎日3時に実行
     * 昨日から今日までの映画をDiscover
     */

    @Scheduled(cron = "0 0 3 * * ?", zone = "Asia/Tokyo")
    public void runDaily() throws Exception {
        LocalDate today     = LocalDate.now().minusDays(1);
        LocalDate yesterday = today.minusDays(2);

        var params = new JobParametersBuilder()
                .addString("from", yesterday.toString())
                .addString("to",   today.toString())
                .addLong("run.id", System.currentTimeMillis())  // ユニークID
                .toJobParameters();  // パラメータ生成

        jobLauncher.run(discoverJob, params);  // バッチ実行
    }
}