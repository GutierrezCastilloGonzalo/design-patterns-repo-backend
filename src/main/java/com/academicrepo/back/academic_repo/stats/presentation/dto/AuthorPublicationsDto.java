package com.academicrepo.back.academic_repo.stats.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorPublicationsDto {
    private Long authorId;
    private String fullName;
    private String affiliation;
    private Long publicationCount;
}
