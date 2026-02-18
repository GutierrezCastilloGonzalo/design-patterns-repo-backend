package com.academicrepo.back.academic_repo.users.application.commands.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.users.application.commands.ActivateUserCommand;
import com.academicrepo.back.academic_repo.users.domain.entities.DUser;
import com.academicrepo.back.academic_repo.users.domain.repositories.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ActivateUserCommandHandler {
    private final IUserRepository userRepository;

    public ActivateUserCommandHandler(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public DUser execute(ActivateUserCommand command) {
        try {
            DUser existingUser = userRepository.findByIdIncludingInactive(command.userId());
            if (existingUser == null) {
                throw new IllegalArgumentException(
                        "Usuario no encontrado con ID: " + command.userId());
            }

            existingUser.setIsActive(true);
            return userRepository.update(existingUser);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
