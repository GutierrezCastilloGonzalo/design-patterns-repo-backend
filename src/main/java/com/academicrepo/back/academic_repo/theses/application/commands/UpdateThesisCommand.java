package com.academicrepo.back.academic_repo.theses.application.commands;

import com.academicrepo.back.academic_repo.theses.presentation.dto.UpdateThesisDto;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public record UpdateThesisCommand(Long id, UpdateThesisDto dto, @Nullable MultipartFile file) {}
