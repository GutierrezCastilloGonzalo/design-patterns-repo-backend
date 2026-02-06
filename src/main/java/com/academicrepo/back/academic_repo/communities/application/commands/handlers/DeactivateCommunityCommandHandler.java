package com.academicrepo.back.academic_repo.communities.application.commands.handlers;

import com.academicrepo.back.academic_repo.communities.application.commands.DeactivateCommunityCommand;
import com.academicrepo.back.academic_repo.communities.domain.entities.DCommunity;
import com.academicrepo.back.academic_repo.communities.domain.repositories.ICommunityRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeactivateCommunityCommandHandler {

    private final ICommunityRepository repository;

    @Transactional
    public DCommunity execute(DeactivateCommunityCommand command) {
        try {
            DCommunity existing = repository.findById(command.id());
            if (existing == null) {
                throw new IllegalArgumentException(
                        "Comunidad no encontrada con ID: " + command.id());
            }

            existing.setIsActive(false);
            return repository.update(existing);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
