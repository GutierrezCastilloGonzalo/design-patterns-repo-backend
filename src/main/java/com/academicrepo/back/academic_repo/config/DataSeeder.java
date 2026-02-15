package com.academicrepo.back.academic_repo.config;

import com.academicrepo.back.academic_repo.users.infrastructure.entities.Role;
import com.academicrepo.back.academic_repo.users.infrastructure.entities.User;
import com.academicrepo.back.academic_repo.users.infrastructure.entities.UserRole;
import com.academicrepo.back.academic_repo.users.infrastructure.repositories.interfaces.IRoleJpaRepository;
import com.academicrepo.back.academic_repo.users.infrastructure.repositories.interfaces.IUserJpaRepository;
import com.academicrepo.back.academic_repo.users.infrastructure.repositories.interfaces.IUserRoleJpaRepository;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private static final String SUPERADMIN_EMAIL = "superadmin@academicrepo.com";
    private static final String SUPERADMIN_USERNAME = "superadmin";
    private static final String SUPERADMIN_PASSWORD = "SuperAdmin123!";

    private static final Map<String, String> DEFAULT_ROLES =
            Map.of(
                    "SuperAdmin", "Administrador con acceso total al sistema",
                    "Autor", "Usuario que puede crear y gestionar tesis",
                    "Bibliotecario", "Usuario encargado de la gestión de colecciones",
                    "Coordinador", "Usuario que coordina comunidades y subcomunidades",
                    "Usuario", "Usuario básico con acceso de lectura");

    @Bean
    CommandLineRunner initData(
            IUserJpaRepository userRepository,
            IRoleJpaRepository roleRepository,
            IUserRoleJpaRepository userRoleRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            seedRoles(roleRepository);
            seedSuperAdmin(userRepository, roleRepository, userRoleRepository, passwordEncoder);
        };
    }

    private void seedRoles(IRoleJpaRepository roleRepository) {
        DEFAULT_ROLES.forEach(
                (name, description) -> {
                    if (!roleRepository.existsByName(name)) {
                        Role role =
                                Role.builder()
                                        .name(name)
                                        .description(description)
                                        .isActive(true)
                                        .build();
                        roleRepository.save(role);
                        logger.info("Role '{}' created successfully.", name);
                    }
                });
    }

    private void seedSuperAdmin(
            IUserJpaRepository userRepository,
            IRoleJpaRepository roleRepository,
            IUserRoleJpaRepository userRoleRepository,
            PasswordEncoder passwordEncoder) {

        final User superadminUser;

        if (userRepository.existsByEmail(SUPERADMIN_EMAIL)) {
            logger.info("Superadmin user already exists, skipping user creation.");
            superadminUser = userRepository.findByEmail(SUPERADMIN_EMAIL).orElse(null);
        } else {
            logger.info("Creating superadmin user...");
            superadminUser =
                    userRepository.save(
                            User.builder()
                                    .userName(SUPERADMIN_USERNAME)
                                    .email(SUPERADMIN_EMAIL)
                                    .passwordHash(passwordEncoder.encode(SUPERADMIN_PASSWORD))
                                    .isActive(true)
                                    .build());
            logger.info("Superadmin user created successfully!");
            logger.info("Email: {}", SUPERADMIN_EMAIL);
            logger.info("Password: {}", SUPERADMIN_PASSWORD);
            logger.warn("IMPORTANT: Please change the superadmin password after first login!");
        }

        if (superadminUser != null) {
            roleRepository
                    .findByName("SuperAdmin")
                    .ifPresent(
                            role -> {
                                if (!userRoleRepository.existsByUserIdAndRoleId(
                                        superadminUser.getId(), role.getId())) {
                                    UserRole userRole =
                                            UserRole.builder()
                                                    .userId(superadminUser.getId())
                                                    .roleId(role.getId())
                                                    .build();
                                    userRoleRepository.save(userRole);
                                    logger.info(
                                            "SuperAdmin role assigned to user '{}'.",
                                            SUPERADMIN_EMAIL);
                                }
                            });
        }
    }
}
