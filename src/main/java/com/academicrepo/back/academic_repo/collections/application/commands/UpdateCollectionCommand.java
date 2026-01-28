package com.academicrepo.back.academic_repo.collections.application.commands;

import com.academicrepo.back.academic_repo.collections.presentation.dto.UpdateCollectionDto;

public record UpdateCollectionCommand(Long id, UpdateCollectionDto dto) {
}
