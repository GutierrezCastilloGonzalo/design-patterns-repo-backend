package com.academicrepo.back.academic_repo.stats.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdvisorThesesDto {
    private Long advisorId;
    private String fullName;
    private String department;
    private String advisorTitle;
    private Long thesesCount;
}
