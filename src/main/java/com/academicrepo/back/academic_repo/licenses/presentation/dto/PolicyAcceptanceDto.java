package com.academicrepo.back.academic_repo.licenses.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyAcceptanceDto {

    @NotNull(message = "El ID de la politica es requerido")
    private Long policyId;

    @NotNull(message = "El estado de aceptacion es requerido")
    private Boolean accepted;
}
