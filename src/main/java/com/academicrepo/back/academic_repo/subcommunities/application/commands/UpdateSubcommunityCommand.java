package com.academicrepo.back.academic_repo.subcommunities.application.commands;

import com.academicrepo.back.academic_repo.subcommunities.presentation.dto.UpdateSubcommunityDto;

public record UpdateSubcommunityCommand(Long id, UpdateSubcommunityDto dto) {}
