package com.academicrepo.back.academic_repo.collections.application.commands.handlers;

import com.academicrepo.back.academic_repo.collections.application.commands.UpdateCollectionCommand;
import com.academicrepo.back.academic_repo.collections.domain.entities.DCollection;
import com.academicrepo.back.academic_repo.collections.domain.repositories.ICollectionRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCollectionCommandHandler {

    private final ICollectionRepository repository;

    @Transactional
    public DCollection execute(UpdateCollectionCommand command) {
        try {
            DCollection existing = repository.findById(command.id());
            if (existing == null) {
                throw new IllegalArgumentException(
                        "Coleccion no encontrada con ID: " + command.id());
            }

            if (command.dto().getName() != null) {
                if (!existing.getName().equals(command.dto().getName())
                        && repository.existsByNameAndSubcommunityId(
                                command.dto().getName(), existing.getSubcommunityId())) {
                    throw new IllegalArgumentException(
                            "Ya existe una coleccion con ese nombre en esta subcomunidad");
                }
                existing.setName(command.dto().getName());
            }
            if (command.dto().getDescription() != null) {
                existing.setDescription(command.dto().getDescription());
            }

            existing.validate();
            return repository.update(existing);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
