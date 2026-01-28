package com.academicrepo.back.academic_repo.collections.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.academicrepo.back.academic_repo.collections.application.commands.DeactivateCollectionCommand;
import com.academicrepo.back.academic_repo.collections.domain.entities.DCollection;
import com.academicrepo.back.academic_repo.collections.domain.repositories.ICollectionRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeactivateCollectionCommandHandler {

    private final ICollectionRepository repository;

    @Transactional
    public DCollection execute(DeactivateCollectionCommand command) {
        try {
            DCollection existing = repository.findById(command.id());
            if (existing == null) {
                throw new IllegalArgumentException("Coleccion no encontrada con ID: " + command.id());
            }

            existing.setIsActive(false);
            return repository.update(existing);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
