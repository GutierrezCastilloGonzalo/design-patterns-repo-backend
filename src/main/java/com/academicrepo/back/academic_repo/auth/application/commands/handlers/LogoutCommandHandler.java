package com.academicrepo.back.academic_repo.auth.application.commands.handlers;

import com.academicrepo.back.academic_repo.auth.application.commands.LogoutCommand;
import com.academicrepo.back.academic_repo.auth.domain.entities.DSession;
import com.academicrepo.back.academic_repo.auth.domain.repositories.ISessionRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutCommandHandler {

    private final ISessionRepository sessionRepository;

    @Transactional
    public void execute(LogoutCommand command) {
        try {
            var sessionOpt = sessionRepository.findByToken(command.token());
            if (sessionOpt.isPresent()) {
                DSession session = sessionOpt.get();
                session.setIsRevoked(true);
                sessionRepository.update(session);
            }
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
