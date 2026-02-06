package com.academicrepo.back.academic_repo.auth.application.commands;

import com.academicrepo.back.academic_repo.auth.presentation.dto.RefreshTokenRequestDto;

public record RefreshTokenCommand(
        RefreshTokenRequestDto refreshTokenDto, String userAgent, String ipAddress) {}
