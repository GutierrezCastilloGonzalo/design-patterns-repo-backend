package com.academicrepo.back.academic_repo.collections.application.commands.handlers;

import com.academicrepo.back.academic_repo.collections.application.commands.CreateCollectionCommand;
import com.academicrepo.back.academic_repo.collections.domain.entities.DCollection;
import com.academicrepo.back.academic_repo.collections.domain.repositories.ICollectionRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.subcommunities.domain.repositories.ISubcommunityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCollectionCommandHandler {

    private final ICollectionRepository repository;
    private final ISubcommunityRepository subcommunityRepository;

    @Transactional
    public DCollection execute(CreateCollectionCommand command) {
        try {
            if (subcommunityRepository.findById(command.dto().getSubcommunityId()) == null) {
                throw new IllegalArgumentException(
                        "Subcomunidad no encontrada con ID: " + command.dto().getSubcommunityId());
            }

            if (repository.existsByNameAndSubcommunityId(
                    command.dto().getName(), command.dto().getSubcommunityId())) {
                throw new IllegalArgumentException(
                        "Ya existe una coleccion con ese nombre en esta subcomunidad");
            }

            DCollection collection = new DCollection();
            collection.setName(command.dto().getName());
            collection.setDescription(command.dto().getDescription());
            collection.setSubcommunityId(command.dto().getSubcommunityId());
            collection.validate();

            return repository.save(collection);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
