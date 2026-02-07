package com.academicrepo.back.academic_repo.authors.application.commands.handlers;

import com.academicrepo.back.academic_repo.authors.application.commands.DeactivateAuthorCommand;
import com.academicrepo.back.academic_repo.authors.domain.entities.DAuthor;
import com.academicrepo.back.academic_repo.authors.domain.repositories.IAuthorRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeactivateAuthorCommandHandler {

    private final IAuthorRepository repository;

    @Transactional
    public DAuthor execute(DeactivateAuthorCommand command) {
        try {
            DAuthor existing = repository.findById(command.id());
            if (existing == null) {
                throw new IllegalArgumentException("Autor no encontrado con ID: " + command.id());
            }

            existing.setIsActive(false);
            return repository.update(existing);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
