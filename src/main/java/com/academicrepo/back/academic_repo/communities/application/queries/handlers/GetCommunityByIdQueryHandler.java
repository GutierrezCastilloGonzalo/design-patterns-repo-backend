package com.academicrepo.back.academic_repo.communities.application.queries.handlers;

import com.academicrepo.back.academic_repo.communities.application.queries.GetCommunityByIdQuery;
import com.academicrepo.back.academic_repo.communities.domain.entities.DCommunity;
import com.academicrepo.back.academic_repo.communities.domain.repositories.ICommunityRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCommunityByIdQueryHandler {

    private final ICommunityRepository repository;

    public DCommunity execute(GetCommunityByIdQuery query) {
        try {
            DCommunity community = repository.findById(query.id());
            if (community == null) {
                throw new IllegalArgumentException("Comunidad no encontrada con ID: " + query.id());
            }
            return community;
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
