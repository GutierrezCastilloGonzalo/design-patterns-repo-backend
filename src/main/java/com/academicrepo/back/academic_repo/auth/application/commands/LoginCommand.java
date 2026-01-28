package com.academicrepo.back.academic_repo.auth.application.commands;

import com.academicrepo.back.academic_repo.auth.presentation.dto.LoginRequestDto;

public record LoginCommand(LoginRequestDto loginDto, String userAgent, String ipAddress) {
}
