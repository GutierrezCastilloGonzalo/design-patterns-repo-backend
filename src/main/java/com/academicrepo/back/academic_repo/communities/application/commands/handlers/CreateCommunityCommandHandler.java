package com.academicrepo.back.academic_repo.communities.application.commands.handlers;

import com.academicrepo.back.academic_repo.communities.application.commands.CreateCommunityCommand;
import com.academicrepo.back.academic_repo.communities.domain.entities.DCommunity;
import com.academicrepo.back.academic_repo.communities.domain.repositories.ICommunityRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCommunityCommandHandler {

    private final ICommunityRepository repository;

    @Transactional
    public DCommunity execute(CreateCommunityCommand command) {
        try {
            if (repository.existsByName(command.dto().getName())) {
                throw new IllegalArgumentException(
                        "Ya existe una comunidad con el nombre: " + command.dto().getName());
            }

            DCommunity community = new DCommunity();
            community.setName(command.dto().getName());
            community.setDescription(command.dto().getDescription());
            community.setLogoUrl(command.dto().getLogoUrl());
            community.validate();

            return repository.save(community);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
