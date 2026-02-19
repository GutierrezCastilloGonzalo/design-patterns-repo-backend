package com.academicrepo.back.academic_repo.storage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cloudflare.r2")
public class R2Properties {

    private String accountId;
    private String accessKeyId;
    private String secretAccessKey;
    private String bucketName;
    private String publicUrl;

    public String getEndpoint() {
        return String.format("https://%s.r2.cloudflarestorage.com", accountId);
    }

    public boolean isConfigured() {
        return accountId != null
                && !accountId.isBlank()
                && accessKeyId != null
                && !accessKeyId.isBlank()
                && secretAccessKey != null
                && !secretAccessKey.isBlank()
                && bucketName != null
                && !bucketName.isBlank();
    }
}
