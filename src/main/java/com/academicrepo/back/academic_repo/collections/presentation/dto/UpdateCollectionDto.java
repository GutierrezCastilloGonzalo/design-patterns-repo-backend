package com.academicrepo.back.academic_repo.collections.presentation.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCollectionDto {

    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    private String name;

    private String description;
}
