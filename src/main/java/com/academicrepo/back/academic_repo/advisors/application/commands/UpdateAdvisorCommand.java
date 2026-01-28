package com.academicrepo.back.academic_repo.advisors.application.commands;

import com.academicrepo.back.academic_repo.advisors.presentation.dto.UpdateAdvisorDto;

public record UpdateAdvisorCommand(Long id, UpdateAdvisorDto dto) {
}
