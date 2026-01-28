package com.academicrepo.back.academic_repo.subcommunities.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.academicrepo.back.academic_repo.communities.domain.repositories.ICommunityRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.subcommunities.application.commands.CreateSubcommunityCommand;
import com.academicrepo.back.academic_repo.subcommunities.domain.entities.DSubcommunity;
import com.academicrepo.back.academic_repo.subcommunities.domain.repositories.ISubcommunityRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateSubcommunityCommandHandler {

    private final ISubcommunityRepository repository;
    private final ICommunityRepository communityRepository;

    @Transactional
    public DSubcommunity execute(CreateSubcommunityCommand command) {
        try {
            if (communityRepository.findById(command.dto().getCommunityId()) == null) {
                throw new IllegalArgumentException("Comunidad no encontrada con ID: " + command.dto().getCommunityId());
            }

            if (repository.existsByNameAndCommunityId(command.dto().getName(), command.dto().getCommunityId())) {
                throw new IllegalArgumentException("Ya existe una subcomunidad con ese nombre en esta comunidad");
            }

            DSubcommunity subcommunity = new DSubcommunity();
            subcommunity.setName(command.dto().getName());
            subcommunity.setDescription(command.dto().getDescription());
            subcommunity.setCommunityId(command.dto().getCommunityId());
            subcommunity.validate();

            return repository.save(subcommunity);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
