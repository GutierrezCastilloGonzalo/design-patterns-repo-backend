package com.academicrepo.back.academic_repo.users.application.commands.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.users.application.commands.HardDeleteUserCommand;
import com.academicrepo.back.academic_repo.users.domain.entities.DUser;
import com.academicrepo.back.academic_repo.users.domain.repositories.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class HardDeleteUserCommandHandler {
    private final IUserRepository userRepository;

    public HardDeleteUserCommandHandler(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void execute(HardDeleteUserCommand command) {
        try {
            DUser existingUser = userRepository.findByIdIncludingInactive(command.userId());
            if (existingUser == null) {
                throw new IllegalArgumentException(
                        "Usuario no encontrado con ID: " + command.userId());
            }

            userRepository.deleteById(command.userId());
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
