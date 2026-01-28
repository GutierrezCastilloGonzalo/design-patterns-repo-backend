package com.academicrepo.back.academic_repo.authors.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAuthorDto {

    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String firstName;

    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String lastName;

    @Email(message = "El email debe tener un formato valido")
    @Size(max = 255, message = "El email no puede exceder 255 caracteres")
    private String email;

    @Size(max = 50, message = "El ORCID no puede exceder 50 caracteres")
    private String orcid;

    @Size(max = 255, message = "La afiliacion no puede exceder 255 caracteres")
    private String affiliation;
}
