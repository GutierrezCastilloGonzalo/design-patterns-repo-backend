package com.academicrepo.back.academic_repo.theses.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThesisTopDownloadedDto {
    private String id;
    private String title;
    private Long nDescargas;
}
