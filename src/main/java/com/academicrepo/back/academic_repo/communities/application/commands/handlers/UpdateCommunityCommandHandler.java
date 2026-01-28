package com.academicrepo.back.academic_repo.communities.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.academicrepo.back.academic_repo.communities.application.commands.UpdateCommunityCommand;
import com.academicrepo.back.academic_repo.communities.domain.entities.DCommunity;
import com.academicrepo.back.academic_repo.communities.domain.repositories.ICommunityRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateCommunityCommandHandler {

    private final ICommunityRepository repository;

    @Transactional
    public DCommunity execute(UpdateCommunityCommand command) {
        try {
            DCommunity existing = repository.findById(command.id());
            if (existing == null) {
                throw new IllegalArgumentException("Comunidad no encontrada con ID: " + command.id());
            }

            if (command.dto().getName() != null) {
                if (!existing.getName().equals(command.dto().getName()) && repository.existsByName(command.dto().getName())) {
                    throw new IllegalArgumentException("Ya existe una comunidad con el nombre: " + command.dto().getName());
                }
                existing.setName(command.dto().getName());
            }
            if (command.dto().getDescription() != null) {
                existing.setDescription(command.dto().getDescription());
            }
            if (command.dto().getLogoUrl() != null) {
                existing.setLogoUrl(command.dto().getLogoUrl());
            }

            existing.validate();
            return repository.update(existing);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
