package com.academicrepo.back.academic_repo.users.application.commands;

import com.academicrepo.back.academic_repo.users.presentation.dto.CreateUserDto;

public record CreateUserCommand(CreateUserDto userDto) {
}
