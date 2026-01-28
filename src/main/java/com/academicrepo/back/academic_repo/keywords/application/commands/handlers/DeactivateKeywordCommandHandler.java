package com.academicrepo.back.academic_repo.keywords.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.keywords.application.commands.DeactivateKeywordCommand;
import com.academicrepo.back.academic_repo.keywords.domain.entities.DKeyword;
import com.academicrepo.back.academic_repo.keywords.domain.repositories.IKeywordRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeactivateKeywordCommandHandler {

    private final IKeywordRepository repository;

    @Transactional
    public DKeyword execute(DeactivateKeywordCommand command) {
        try {
            DKeyword existing = repository.findById(command.id());
            if (existing == null) {
                throw new IllegalArgumentException("Palabra clave no encontrada con ID: " + command.id());
            }

            existing.setIsActive(false);
            return repository.update(existing);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
