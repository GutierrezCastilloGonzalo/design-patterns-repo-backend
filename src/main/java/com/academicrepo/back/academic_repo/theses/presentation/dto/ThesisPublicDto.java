package com.academicrepo.back.academic_repo.theses.presentation.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThesisPublicDto {
    private Long id;
    private Boolean isActive;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String title;
    private String abstractText;
    private LocalDate publicationDate;
    private String fileUrl;
    private String thumbnailUrl;
    private Integer numberOfPages;
    private String language;
    private String documentType;
    private String doi;
    private String license;
    private String collection;
    private List<String> authors;
    private List<String> keywords;
    private Long nDescargas;
    private Long nVistas;
}
