package com.academicrepo.back.academic_repo.communities.application.commands;

import com.academicrepo.back.academic_repo.communities.presentation.dto.UpdateCommunityDto;

public record UpdateCommunityCommand(Long id, UpdateCommunityDto dto) {}
