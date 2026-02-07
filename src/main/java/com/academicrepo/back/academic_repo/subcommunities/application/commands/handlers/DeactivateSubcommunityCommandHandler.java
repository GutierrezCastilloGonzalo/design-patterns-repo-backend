package com.academicrepo.back.academic_repo.subcommunities.application.commands.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.subcommunities.application.commands.DeactivateSubcommunityCommand;
import com.academicrepo.back.academic_repo.subcommunities.domain.entities.DSubcommunity;
import com.academicrepo.back.academic_repo.subcommunities.domain.repositories.ISubcommunityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeactivateSubcommunityCommandHandler {

    private final ISubcommunityRepository repository;

    @Transactional
    public DSubcommunity execute(DeactivateSubcommunityCommand command) {
        try {
            DSubcommunity existing = repository.findById(command.id());
            if (existing == null) {
                throw new IllegalArgumentException(
                        "Subcomunidad no encontrada con ID: " + command.id());
            }

            existing.setIsActive(false);
            return repository.update(existing);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
