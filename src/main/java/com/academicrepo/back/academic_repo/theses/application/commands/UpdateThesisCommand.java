package com.academicrepo.back.academic_repo.theses.application.commands;

import com.academicrepo.back.academic_repo.theses.presentation.dto.UpdateThesisDto;

public record UpdateThesisCommand(Long id, UpdateThesisDto dto) {
}
