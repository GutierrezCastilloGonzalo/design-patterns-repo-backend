package com.academicrepo.back.academic_repo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.academicrepo.back.academic_repo.users.infrastructure.entities.User;
import com.academicrepo.back.academic_repo.users.infrastructure.repositories.interfaces.IUserJpaRepository;

/**
 * Data seeder configuration for initializing default data on application
 * startup.
 * Creates a superadmin user if it doesn't already exist.
 */
@Configuration
public class DataSeeder {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private static final String SUPERADMIN_EMAIL = "superadmin@academicrepo.com";
    private static final String SUPERADMIN_USERNAME = "superadmin";
    private static final String SUPERADMIN_PASSWORD = "SuperAdmin123!";

    @Bean
    CommandLineRunner initSuperAdmin(IUserJpaRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.existsByEmail(SUPERADMIN_EMAIL)) {
                logger.info("Superadmin user already exists, skipping seed.");
                return;
            }

            logger.info("Creating superadmin user...");

            User superadminUser = User.builder()
                    .userName(SUPERADMIN_USERNAME)
                    .email(SUPERADMIN_EMAIL)
                    .passwordHash(passwordEncoder.encode(SUPERADMIN_PASSWORD))
                    .isActive(true)
                    .build();

            userRepository.save(superadminUser);

            logger.info("Superadmin user created successfully!");
            logger.info("Email: {}", SUPERADMIN_EMAIL);
            logger.info("Password: {}", SUPERADMIN_PASSWORD);
            logger.warn("IMPORTANT: Please change the superadmin password after first login!");
        };
    }
}
