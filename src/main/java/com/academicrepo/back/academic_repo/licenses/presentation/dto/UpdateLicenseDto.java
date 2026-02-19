package com.academicrepo.back.academic_repo.licenses.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLicenseDto {

    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String name;

    private String description;

    @Size(max = 50, message = "El tipo no puede exceder 50 caracteres")
    private String type;

    @Size(max = 20, message = "El estado no puede exceder 20 caracteres")
    private String status;

    @Size(max = 20, message = "La version no puede exceder 20 caracteres")
    private String version;

    @Size(max = 500, message = "La URL del archivo no puede exceder 500 caracteres")
    private String fileUrl;

    private LocalDate validFrom;

    private LocalDate validUntil;

    @Valid private List<PolicyAcceptanceDto> policyAcceptances;
}
