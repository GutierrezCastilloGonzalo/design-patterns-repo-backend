package com.academicrepo.back.academic_repo.subcommunities.application.queries.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.subcommunities.application.queries.GetSubcommunityByIdQuery;
import com.academicrepo.back.academic_repo.subcommunities.domain.entities.DSubcommunity;
import com.academicrepo.back.academic_repo.subcommunities.domain.repositories.ISubcommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSubcommunityByIdQueryHandler {

    private final ISubcommunityRepository repository;

    public DSubcommunity execute(GetSubcommunityByIdQuery query) {
        try {
            DSubcommunity subcommunity = repository.findById(query.id());
            if (subcommunity == null) {
                throw new IllegalArgumentException(
                        "Subcomunidad no encontrada con ID: " + query.id());
            }
            return subcommunity;
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
