package com.academicrepo.back.academic_repo.keywords.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateKeywordDto {

    @NotBlank(message = "La palabra clave es requerida")
    @Size(max = 100, message = "La palabra clave no puede exceder 100 caracteres")
    private String word;
}
