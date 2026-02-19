package com.academicrepo.back.academic_repo.storage.presentation.controllers;

import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;
import com.academicrepo.back.academic_repo.storage.application.StorageService;
import com.academicrepo.back.academic_repo.storage.presentation.dto.FileUploadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "Files", description = "Gestion de archivos - subida y eliminacion")
@SecurityRequirement(name = "bearerAuth")
public class FileController extends BaseV1Controller {

    private final StorageService storageService;

    @GetMapping("/status")
    @Operation(
            summary = "Estado del servicio de almacenamiento",
            description = "Verifica si el servicio de Cloudflare R2 esta configurado y disponible")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado del servicio"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<Map<String, Object>> getStatus() {
        boolean available = storageService.isAvailable();
        return ResponseEntity.ok(
                Map.of(
                        "available",
                        available,
                        "message",
                        available
                                ? "Servicio de almacenamiento disponible"
                                : "Servicio de almacenamiento no configurado. Configure las variables CLOUDFLARE_R2_*"));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Subir archivo",
            description =
                    "Sube un archivo a Cloudflare R2. Soporta archivos hasta 50MB. "
                            + "Folders disponibles: theses, licenses, documents")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Archivo subido exitosamente",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = FileUploadResponse.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Archivo invalido o excede el tamano maximo"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<FileUploadResponse> uploadFile(
            @Parameter(description = "Archivo a subir", required = true) @RequestParam("file")
                    MultipartFile file,
            @Parameter(
                            description = "Carpeta destino (theses, licenses, documents)",
                            example = "theses")
                    @RequestParam(value = "folder", defaultValue = "documents")
                    String folder) {

        FileUploadResponse response = storageService.uploadFile(file, sanitizeFolder(folder));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Eliminar archivo",
            description = "Elimina un archivo de Cloudflare R2 usando su URL publica")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Archivo eliminado exitosamente"),
        @ApiResponse(responseCode = "400", description = "URL invalida"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "500", description = "Error al eliminar el archivo")
    })
    public ResponseEntity<Void> deleteFile(
            @Parameter(description = "URL publica del archivo a eliminar", required = true)
                    @RequestParam("fileUrl")
                    String fileUrl) {

        storageService.deleteFile(fileUrl);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download")
    @Operation(
            summary = "Descargar archivo",
            description =
                    "Descarga un archivo de Cloudflare R2 usando su URL publica como proxy autenticado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Archivo descargado exitosamente"),
        @ApiResponse(responseCode = "400", description = "URL invalida"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "500", description = "Error al descargar el archivo")
    })
    public ResponseEntity<InputStreamResource> downloadFile(
            @Parameter(description = "URL publica del archivo a descargar", required = true)
                    @RequestParam("fileUrl")
                    String fileUrl) {

        ResponseInputStream<GetObjectResponse> r2Response = storageService.downloadFile(fileUrl);
        GetObjectResponse metadata = r2Response.response();

        String contentType =
                metadata.contentType() != null
                        ? metadata.contentType()
                        : "application/octet-stream";

        String filename = extractFilenameFromUrl(fileUrl);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(new InputStreamResource(r2Response));
    }

    private String extractFilenameFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return "archivo";
        }
        int lastSlash = fileUrl.lastIndexOf('/');
        if (lastSlash >= 0 && lastSlash < fileUrl.length() - 1) {
            return fileUrl.substring(lastSlash + 1);
        }
        return "archivo";
    }

    private String sanitizeFolder(String folder) {
        // Remove leading/trailing slashes and prevent directory traversal
        String sanitized = folder.replaceAll("^/+|/+$", "").replaceAll("\\.\\.", "");

        // Only allow specific folders
        return switch (sanitized.toLowerCase()) {
            case "theses" -> "theses";
            case "licenses" -> "licenses";
            case "documents" -> "documents";
            default -> "documents";
        };
    }
}
