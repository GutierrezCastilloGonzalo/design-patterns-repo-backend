package com.academicrepo.back.academic_repo.storage.application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfThumbnailService {

    private static final int THUMBNAIL_DPI = 150;
    private static final String THUMBNAIL_FOLDER = "thumbnails";

    private final StorageService storageService;

    /**
     * Generates a PNG thumbnail from the first page of a PDF and uploads it to R2.
     *
     * @param file The PDF file to render
     * @return The public URL of the uploaded thumbnail, or null if generation fails
     */
    @Nullable
    public String generateAndUpload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equalsIgnoreCase("application/pdf")) {
            return null;
        }

        if (!storageService.isAvailable()) {
            log.warn("Storage not available, skipping thumbnail generation");
            return null;
        }

        try {
            byte[] pdfBytes = file.getBytes();

            try (PDDocument document = Loader.loadPDF(pdfBytes)) {
                PDFRenderer renderer = new PDFRenderer(document);
                BufferedImage image = renderer.renderImageWithDPI(0, THUMBNAIL_DPI);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "PNG", baos);
                byte[] thumbnailBytes = baos.toByteArray();

                String filename =
                        String.format(
                                "%d_%s.png", System.currentTimeMillis(), UUID.randomUUID());

                return storageService
                        .uploadBytes(thumbnailBytes, "image/png", filename, THUMBNAIL_FOLDER)
                        .getFileUrl();
            }
        } catch (Exception e) {
            log.error("Failed to generate PDF thumbnail for file: {}", file.getOriginalFilename(), e);
            return null;
        }
    }
}
