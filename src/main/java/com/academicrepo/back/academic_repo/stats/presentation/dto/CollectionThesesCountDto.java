package com.academicrepo.back.academic_repo.stats.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CollectionThesesCountDto {
    private Long collectionId;
    private String collectionName;
    private Long thesesCount;
}
