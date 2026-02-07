package com.academicrepo.back.academic_repo.users.application.commands;

import com.academicrepo.back.academic_repo.users.presentation.dto.UpdateUserDto;

public record UpdateUserCommand(Long userId, UpdateUserDto userDto) {}
