package com.academicrepo.back.academic_repo.theses.presentation.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateThesisDto {

    @Size(max = 500, message = "El título no puede exceder 500 caracteres")
    private String title;

    private String abstractText;

    private LocalDate publicationDate;

    @Size(max = 500, message = "La URL del archivo no puede exceder 500 caracteres")
    private String fileUrl;

    private Integer numberOfPages;

    @Size(max = 50, message = "El idioma no puede exceder 50 caracteres")
    private String language;

    @Size(max = 100, message = "El tipo de documento no puede exceder 100 caracteres")
    private String documentType;

    @Size(max = 100, message = "El DOI no puede exceder 100 caracteres")
    private String doi;

    @Size(max = 100, message = "La licencia no puede exceder 100 caracteres")
    private String license;

    private Long advisorId;

    private List<Long> authorIds;

    private List<Long> keywordIds;
}
