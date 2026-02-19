package com.academicrepo.back.academic_repo.theses.application.commands;

import com.academicrepo.back.academic_repo.theses.presentation.dto.CreateThesisDto;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public record CreateThesisCommand(CreateThesisDto dto, @Nullable MultipartFile file) {}
