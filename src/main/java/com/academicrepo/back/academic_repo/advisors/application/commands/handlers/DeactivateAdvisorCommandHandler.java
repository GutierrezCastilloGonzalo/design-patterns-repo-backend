package com.academicrepo.back.academic_repo.advisors.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.academicrepo.back.academic_repo.advisors.application.commands.DeactivateAdvisorCommand;
import com.academicrepo.back.academic_repo.advisors.domain.entities.DAdvisor;
import com.academicrepo.back.academic_repo.advisors.domain.repositories.IAdvisorRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeactivateAdvisorCommandHandler {

    private final IAdvisorRepository repository;

    @Transactional
    public DAdvisor execute(DeactivateAdvisorCommand command) {
        try {
            DAdvisor existing = repository.findById(command.id());
            if (existing == null) {
                throw new IllegalArgumentException("Asesor no encontrado con ID: " + command.id());
            }

            existing.setIsActive(false);
            return repository.update(existing);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
