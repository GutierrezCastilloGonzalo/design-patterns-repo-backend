package com.academicrepo.back.academic_repo.users.application.commands.handlers;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.users.application.commands.UpdateUserCommand;
import com.academicrepo.back.academic_repo.users.domain.entities.DUser;
import com.academicrepo.back.academic_repo.users.domain.repositories.IUserRepository;

import jakarta.transaction.Transactional;

@Service
public class UpdateUserCommandHandler {
    private final IUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UpdateUserCommandHandler(IUserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public DUser execute(UpdateUserCommand command) {
        try {
            DUser existingUser = userRepository.findById(command.userId());
            if (existingUser == null) {
                throw new IllegalArgumentException("Usuario no encontrado con ID: " + command.userId());
            }

            if (command.userDto().getUserName() != null) {
                existingUser.setUserName(Optional.of(command.userDto().getUserName()));
            }

            if (command.userDto().getEmail() != null) {
                if (!existingUser.getEmail().equals(command.userDto().getEmail()) &&
                    userRepository.existsByEmail(command.userDto().getEmail())) {
                    throw new IllegalArgumentException("Ya existe un usuario con el email: " + command.userDto().getEmail());
                }
                existingUser.setEmail(command.userDto().getEmail());
                existingUser.validateEmail();
            }

            if (command.userDto().getPassword() != null) {
                existingUser.validatePasswordStrength(command.userDto().getPassword());
                String hashedPassword = passwordEncoder.encode(command.userDto().getPassword());
                existingUser.setPasswordHash(hashedPassword);
            }

            return userRepository.update(existingUser);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
