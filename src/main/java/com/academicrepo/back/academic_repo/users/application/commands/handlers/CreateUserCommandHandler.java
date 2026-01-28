package com.academicrepo.back.academic_repo.users.application.commands.handlers;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.users.application.commands.CreateUserCommand;
import com.academicrepo.back.academic_repo.users.application.events.UserCreatedDomainEvent;
import com.academicrepo.back.academic_repo.users.domain.entities.DUser;
import com.academicrepo.back.academic_repo.users.domain.repositories.IUserRepository;

import jakarta.transaction.Transactional;

@Service
public class CreateUserCommandHandler {
    private final IUserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final BCryptPasswordEncoder passwordEncoder;

    public CreateUserCommandHandler(IUserRepository userRepository,
                                   ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public DUser execute(CreateUserCommand command) {
        try {
            if (userRepository.existsByEmail(command.userDto().getEmail())) {
                throw new IllegalArgumentException("Ya existe un usuario con el email: " + command.userDto().getEmail());
            }

            DUser newUser = new DUser();
            newUser.setEmail(command.userDto().getEmail());
            newUser.setUserName(command.userDto().getUserName() != null ?
                Optional.of(command.userDto().getUserName()) : Optional.empty());

            newUser.validatePasswordStrength(command.userDto().getPassword());
            String hashedPassword = passwordEncoder.encode(command.userDto().getPassword());
            newUser.setPasswordHash(hashedPassword);

            newUser.validateEmail();

            DUser savedUser = userRepository.save(newUser);

            eventPublisher.publishEvent(
                new UserCreatedDomainEvent(savedUser.getId(), "Se creó un nuevo usuario")
            );

            return savedUser;
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
