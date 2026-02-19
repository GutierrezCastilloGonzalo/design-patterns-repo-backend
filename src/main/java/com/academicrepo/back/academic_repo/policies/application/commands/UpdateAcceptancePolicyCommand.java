package com.academicrepo.back.academic_repo.policies.application.commands;

import com.academicrepo.back.academic_repo.policies.presentation.dto.UpdateAcceptancePolicyDto;

public record UpdateAcceptancePolicyCommand(Long id, UpdateAcceptancePolicyDto dto) {}
