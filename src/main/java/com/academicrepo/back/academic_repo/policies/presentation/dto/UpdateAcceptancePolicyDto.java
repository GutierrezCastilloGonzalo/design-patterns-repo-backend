package com.academicrepo.back.academic_repo.policies.presentation.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAcceptancePolicyDto {

    @Size(max = 50, message = "El codigo no puede exceder 50 caracteres")
    private String code;

    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;

    private String description;

    private Boolean required;
}
