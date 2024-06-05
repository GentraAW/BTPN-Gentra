package com.be_springboot_onlineshop.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${application.minio.url}")
    private String minioUrl;

    @Value("${application.minio.username}")
    private String minioUsername;

    @Value("${application.minio.password}")
    private String minioPassword;

    // @Value("${application.minio.bucketName}")
    // private String minioBucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(minioUsername, minioPassword)
                .build();
    }
}
