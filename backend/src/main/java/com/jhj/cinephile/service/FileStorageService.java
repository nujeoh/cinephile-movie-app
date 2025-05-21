package com.jhj.cinephile.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

/**
 * ファイルストレージサービス
 * - S3バケットへのファイルアップロードと古いファイルの置き換えを行う
 */
@Service
@Slf4j
public class FileStorageService {

    private final S3Client s3Client;
    private final String bucketName;
    private final String region;

    public FileStorageService(
            S3Client s3Client,
            @Value("${S3_BUCKET}") String bucketName,
            @Value("${AWS_REGION}") String region
    ) {
        this.s3Client    = s3Client;
        this.bucketName  = bucketName;
        this.region      = region;
    }
    /**
     * ファイルをアップロードし、既存ファイルを置き換え
     * @param file アップロードするファイル
     * @param oldKey 削除対象の古いファイルキー (存在しない場合はnull可)
     * @return 公開URL
     */
    public String uploadAndReplace(MultipartFile file, String oldKey) throws IOException {
        // バケットが存在しない場合は作成
        if (s3Client.listBuckets().buckets().stream()
                .noneMatch(b -> b.name().equals(bucketName))) {
            s3Client.createBucket(b -> b.bucket(bucketName));
        }

        // 古いオブジェクトを削除 (失敗時は警告ログ)
        if (oldKey != null && !oldKey.isBlank()) {
            try {
                s3Client.deleteObject(b -> b.bucket(bucketName).key(oldKey));
            } catch (Exception e) {
                log.warn("古いファイル削除失敗: {}", oldKey, e);
            }
        }

        // 新しいキーをUUIDで生成
        String newKey = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // S3にアップロード
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(newKey)
                        .contentType(file.getContentType())
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
        );

        // 公開URLを返却
        String base = String.format("https://%s.s3.%s.amazonaws.com", bucketName, region);
        return base + "/" + newKey;
    }

    /**
     * URLからオブジェクトキーを抽出
     * @param url 公開URL
     * @return キー文字列
     */
    public String extractKeyFromUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URLがnullです。");
        }

        URI uri = URI.create(url);
        String path = uri.getPath();

        String bucketPrefix = "/" + bucketName + "/";

        if (path.startsWith(bucketPrefix)) {
            return path.substring(bucketPrefix.length());
        } else if (path.startsWith("/")) {
            return path.substring(1);
        } else {
            return path;
        }
    }

    // エンドポイントURLを返却 (開発環境用)
    private String endpointUrl() {
        return "http://localhost:9000";
    }
}
