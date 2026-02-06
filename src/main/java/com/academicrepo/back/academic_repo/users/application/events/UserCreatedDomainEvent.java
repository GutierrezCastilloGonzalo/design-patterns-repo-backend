package com.academicrepo.back.academic_repo.users.application.events;

public record UserCreatedDomainEvent(Long userId, String message) {}
