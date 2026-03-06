package com.academicrepo.back.academic_repo.stats.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ThesesByYearDto {
    private Integer year;
    private Long count;
}
