package com.academicrepo.back.academic_repo.storage.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponse {

    private String fileName;
    private String originalFileName;
    private String fileUrl;
    private String contentType;
    private Long size;
}
