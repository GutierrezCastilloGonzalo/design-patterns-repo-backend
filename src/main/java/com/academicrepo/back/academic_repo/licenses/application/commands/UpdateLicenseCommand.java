package com.academicrepo.back.academic_repo.licenses.application.commands;

import com.academicrepo.back.academic_repo.licenses.presentation.dto.UpdateLicenseDto;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public record UpdateLicenseCommand(Long id, UpdateLicenseDto dto, @Nullable MultipartFile file) {}
