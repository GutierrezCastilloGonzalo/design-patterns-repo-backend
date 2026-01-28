package com.academicrepo.back.academic_repo.communities.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommunityDto {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    private String name;

    private String description;

    @Size(max = 500, message = "La URL del logo no puede exceder 500 caracteres")
    private String logoUrl;
}
