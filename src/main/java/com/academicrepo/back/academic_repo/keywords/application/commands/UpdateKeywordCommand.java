package com.academicrepo.back.academic_repo.keywords.application.commands;

import com.academicrepo.back.academic_repo.keywords.presentation.dto.UpdateKeywordDto;

public record UpdateKeywordCommand(Long id, UpdateKeywordDto dto) {}
