package com.avijeet.udemybackend.config.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MinioConfig {
    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Bean
    public CommandLineRunner initializeMinio(MinioClient minioClient,
                                             @Value("${minio.bucketName}") String bucketName) {
        return args -> {
            try {
                boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

                if (!found) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                    log.info("MinIO: Created missing bucket: {}", bucketName);
                } else {
                    log.info("MinIO: Bucket already exists : {}", bucketName);
                }
            } catch (Exception e) {
                log.error("MinIO: Initialization failed: {}", e.getMessage());
            }
        };
    }
}
