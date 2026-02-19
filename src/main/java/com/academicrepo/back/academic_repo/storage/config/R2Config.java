package com.academicrepo.back.academic_repo.storage.config;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class R2Config {

    private final R2Properties r2Properties;

    @Bean
    @ConditionalOnProperty(
            prefix = "cloudflare.r2",
            name = {"account-id", "access-key-id", "secret-access-key", "bucket-name"})
    public S3Client s3Client() {
        log.info(
                "Initializing Cloudflare R2 S3 Client for bucket: {}",
                r2Properties.getBucketName());

        AwsBasicCredentials credentials =
                AwsBasicCredentials.create(
                        r2Properties.getAccessKeyId(), r2Properties.getSecretAccessKey());

        return S3Client.builder()
                .endpointOverride(URI.create(r2Properties.getEndpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of("auto"))
                .forcePathStyle(true)
                .build();
    }
}
