package com.avijeet.udemybackend.service.video;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.Http;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoUploadService {
    private final MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    public String uploadAndGetUrl(MultipartFile file, String moduleName) {
        String sanitizedModuleName = moduleName.toLowerCase().replaceAll("[^a-z0-9]", "_");
        String objectName = sanitizedModuleName + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .contentType(file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), -1L)
                            .build()
            );

            log.info("Successfully uploaded the video {} to MinIO", objectName);
            return generatePresignedUrl(objectName);
        } catch (Exception e) {
            log.error("Critical error during video upload: {}", e.getMessage());
            throw new RuntimeException("Critical error during video upload", e);
        }
    }

    private String generatePresignedUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Http.Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(1, TimeUnit.HOURS)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Could not generate signed URL");
        }
    }
}
