package com.academicrepo.back.academic_repo.theses.domain.entities;

import com.academicrepo.back.academic_repo.general.entities.domainEntities.BaseAbstractDomainEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DThesis extends BaseAbstractDomainEntity {
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
    private Long collectionId;
    private Long advisorId;
    private Long nDescargas = 0L;
    private Long nVistas = 0L;
    private List<Long> authorIds = new ArrayList<>();
    private List<Long> keywordIds = new ArrayList<>();

    public void validate() {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la tesis es requerido");
        }
        if (collectionId == null) {
            throw new IllegalArgumentException("La colección es requerida");
        }
    }
}
