package com.academicrepo.back.academic_repo.auth.application.events;

public record UserLoggedInEvent(Long userId, String email, String ipAddress) {}
