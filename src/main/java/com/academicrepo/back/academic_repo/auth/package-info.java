/**
 * Authentication module following Domain-Driven Design (DDD) with CQRS patterns.
 *
 * <p>This module provides JWT-based authentication with session management.
 *
 * <h2>Package Structure:</h2>
 *
 * <ul>
 *   <li><b>domain/</b> - Domain entities, repositories interfaces, and business logic
 *   <li><b>application/</b> - Commands, queries, handlers, and services
 *   <li><b>infrastructure/</b> - JPA entities, repository implementations, mappers
 *   <li><b>presentation/</b> - REST controllers and DTOs
 * </ul>
 */
@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        displayName = "Authentication Module",
        allowedDependencies = {"users", "general", "config"})
package com.academicrepo.back.academic_repo.auth;

import org.springframework.modulith.ApplicationModule;
