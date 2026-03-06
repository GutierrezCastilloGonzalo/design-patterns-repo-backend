package com.academicrepo.back.academic_repo.storage.application;

import com.academicrepo.back.academic_repo.storage.config.R2Properties;
import com.academicrepo.back.academic_repo.storage.presentation.dto.FileUploadResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
public class StorageService {

    @Nullable private final S3Client s3Client;

    private final R2Properties r2Properties;

    public StorageService(
            @Autowired(required = false) @Nullable S3Client s3Client, R2Properties r2Properties) {
        this.s3Client = s3Client;
        this.r2Properties = r2Properties;

        if (s3Client == null) {
            log.warn(
                    "Cloudflare R2 not configured. File upload/download will not be available. "
                            + "Set CLOUDFLARE_R2_* environment variables to enable.");
        } else {
            log.info("Cloudflare R2 storage service initialized");
        }
    }

    /** Checks if the storage service is properly configured and available. */
    public boolean isAvailable() {
        return s3Client != null && r2Properties.isConfigured();
    }

    /**
     * Uploads a file to Cloudflare R2.
     *
     * @param file The file to upload
     * @param folder The folder/prefix to store the file in (e.g., "theses", "licenses")
     * @return FileUploadResponse containing the file URL and metadata
     */
    public FileUploadResponse uploadFile(MultipartFile file, String folder) {
        if (s3Client == null || !r2Properties.isConfigured()) {
            throw new IllegalStateException(
                    "El servicio de almacenamiento no esta configurado. "
                            + "Configure las variables de entorno CLOUDFLARE_R2_*");
        }

        validateFile(file);

        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String uniqueFilename = generateUniqueFilename(extension);
        String key = folder + "/" + uniqueFilename;

        try {
            PutObjectRequest putRequest =
                    PutObjectRequest.builder()
                            .bucket(r2Properties.getBucketName())
                            .key(key)
                            .contentType(file.getContentType())
                            .contentLength(file.getSize())
                            .build();

            s3Client.putObject(
                    putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String fileUrl = buildPublicUrl(key);

            log.info("File uploaded successfully: {} -> {}", originalFilename, fileUrl);

            return FileUploadResponse.builder()
                    .fileName(uniqueFilename)
                    .originalFileName(originalFilename)
                    .fileUrl(fileUrl)
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .build();

        } catch (IOException e) {
            log.error("Failed to upload file: {}", originalFilename, e);
            throw new RuntimeException("Error al subir el archivo: " + e.getMessage(), e);
        }
    }

    /**
     * Downloads a file from Cloudflare R2 by its public URL.
     *
     * @param fileUrl The public URL of the file to download
     * @return ResponseInputStream containing the file data and metadata
     */
    public ResponseInputStream<GetObjectResponse> downloadFile(String fileUrl) {
        if (s3Client == null || !r2Properties.isConfigured()) {
            throw new IllegalStateException(
                    "El servicio de almacenamiento no esta configurado. "
                            + "Configure las variables de entorno CLOUDFLARE_R2_*");
        }

        String key = extractKeyFromUrl(fileUrl);

        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("URL de archivo invalida: " + fileUrl);
        }

        GetObjectRequest getRequest =
                GetObjectRequest.builder().bucket(r2Properties.getBucketName()).key(key).build();

        return s3Client.getObject(getRequest);
    }

    /**
     * Deletes a file from Cloudflare R2.
     *
     * @param fileUrl The public URL of the file to delete
     */
    public void deleteFile(String fileUrl) {
        if (s3Client == null || !r2Properties.isConfigured()) {
            throw new IllegalStateException(
                    "El servicio de almacenamiento no esta configurado. "
                            + "Configure las variables de entorno CLOUDFLARE_R2_*");
        }

        String key = extractKeyFromUrl(fileUrl);

        if (key == null || key.isBlank()) {
            log.warn("Cannot delete file: invalid URL {}", fileUrl);
            return;
        }

        try {
            DeleteObjectRequest deleteRequest =
                    DeleteObjectRequest.builder()
                            .bucket(r2Properties.getBucketName())
                            .key(key)
                            .build();

            s3Client.deleteObject(deleteRequest);
            log.info("File deleted successfully: {}", key);
        } catch (Exception e) {
            log.error("Failed to delete file: {}", key, e);
            throw new RuntimeException("Error al eliminar el archivo: " + e.getMessage(), e);
        }
    }

    /**
     * Uploads raw bytes to Cloudflare R2.
     *
     * @param bytes The file content as bytes
     * @param contentType The MIME type of the content
     * @param filename The filename to use (including extension)
     * @param folder The folder/prefix to store the file in
     * @return FileUploadResponse containing the file URL and metadata
     */
    public FileUploadResponse uploadBytes(
            byte[] bytes, String contentType, String filename, String folder) {
        if (s3Client == null || !r2Properties.isConfigured()) {
            throw new IllegalStateException(
                    "El servicio de almacenamiento no esta configurado. "
                            + "Configure las variables de entorno CLOUDFLARE_R2_*");
        }

        String key = folder + "/" + filename;

        PutObjectRequest putRequest =
                PutObjectRequest.builder()
                        .bucket(r2Properties.getBucketName())
                        .key(key)
                        .contentType(contentType)
                        .contentLength((long) bytes.length)
                        .build();

        s3Client.putObject(putRequest, RequestBody.fromBytes(bytes));

        String fileUrl = buildPublicUrl(key);

        log.info("Bytes uploaded successfully: {} -> {}", filename, fileUrl);

        return FileUploadResponse.builder()
                .fileName(filename)
                .originalFileName(filename)
                .fileUrl(fileUrl)
                .contentType(contentType)
                .size((long) bytes.length)
                .build();
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacio");
        }

        // Max 50MB
        long maxSize = 50 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException(
                    "El archivo excede el tamano maximo permitido (50MB)");
        }
    }

    private String generateUniqueFilename(String extension) {
        String uuid = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        return String.format("%d_%s%s", timestamp, uuid, extension);
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    private String buildPublicUrl(String key) {
        String publicUrl = r2Properties.getPublicUrl();
        if (publicUrl != null && !publicUrl.isBlank()) {
            // Remove trailing slash if present
            if (publicUrl.endsWith("/")) {
                publicUrl = publicUrl.substring(0, publicUrl.length() - 1);
            }
            return publicUrl + "/" + key;
        }
        // Fallback to R2 public bucket URL format
        return String.format("https://pub-%s.r2.dev/%s", r2Properties.getAccountId(), key);
    }

    private String extractKeyFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return null;
        }

        String publicUrl = r2Properties.getPublicUrl();
        if (publicUrl != null && !publicUrl.isBlank() && fileUrl.startsWith(publicUrl)) {
            String key = fileUrl.substring(publicUrl.length());
            if (key.startsWith("/")) {
                key = key.substring(1);
            }
            return key;
        }

        // Try to extract from R2 default URL format
        String r2Pattern = ".r2.dev/";
        int idx = fileUrl.indexOf(r2Pattern);
        if (idx >= 0) {
            return fileUrl.substring(idx + r2Pattern.length());
        }

        return null;
    }
}
