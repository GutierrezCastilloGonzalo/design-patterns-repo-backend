package com.academicrepo.back.academic_repo.licenses.application.commands;

import com.academicrepo.back.academic_repo.licenses.presentation.dto.CreateLicenseDto;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public record CreateLicenseCommand(CreateLicenseDto dto, @Nullable MultipartFile file) {}
