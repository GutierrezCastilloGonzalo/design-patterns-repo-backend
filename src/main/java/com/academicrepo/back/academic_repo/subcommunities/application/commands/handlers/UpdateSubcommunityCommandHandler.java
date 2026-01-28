package com.academicrepo.back.academic_repo.subcommunities.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.subcommunities.application.commands.UpdateSubcommunityCommand;
import com.academicrepo.back.academic_repo.subcommunities.domain.entities.DSubcommunity;
import com.academicrepo.back.academic_repo.subcommunities.domain.repositories.ISubcommunityRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateSubcommunityCommandHandler {

    private final ISubcommunityRepository repository;

    @Transactional
    public DSubcommunity execute(UpdateSubcommunityCommand command) {
        try {
            DSubcommunity existing = repository.findById(command.id());
            if (existing == null) {
                throw new IllegalArgumentException("Subcomunidad no encontrada con ID: " + command.id());
            }

            if (command.dto().getName() != null) {
                if (!existing.getName().equals(command.dto().getName()) &&
                    repository.existsByNameAndCommunityId(command.dto().getName(), existing.getCommunityId())) {
                    throw new IllegalArgumentException("Ya existe una subcomunidad con ese nombre en esta comunidad");
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
