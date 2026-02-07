/**
 * Configuration module for global application configurations. This module contains configuration
 * classes that may depend on other modules.
 */
@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        displayName = "Configuration Module",
        allowedDependencies = {"users", "auth", "general"})
package com.academicrepo.back.academic_repo.config;

import org.springframework.modulith.ApplicationModule;
