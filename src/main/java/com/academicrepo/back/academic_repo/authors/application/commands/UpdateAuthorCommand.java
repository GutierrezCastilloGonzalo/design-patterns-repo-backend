package com.academicrepo.back.academic_repo.authors.application.commands;

import com.academicrepo.back.academic_repo.authors.presentation.dto.UpdateAuthorDto;

public record UpdateAuthorCommand(Long id, UpdateAuthorDto dto) {
}
