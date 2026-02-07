package com.academicrepo.back.academic_repo.theses.application.commands.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.theses.application.commands.DeactivateThesisCommand;
import com.academicrepo.back.academic_repo.theses.domain.entities.DThesis;
import com.academicrepo.back.academic_repo.theses.domain.repositories.IThesisRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeactivateThesisCommandHandler {

    private final IThesisRepository repository;

    @Transactional
    public DThesis execute(DeactivateThesisCommand command) {
        try {
            DThesis existing = repository.findById(command.id());
            if (existing == null) {
                throw new IllegalArgumentException("Tesis no encontrada con ID: " + command.id());
            }

            existing.setIsActive(false);
            return repository.update(existing);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
