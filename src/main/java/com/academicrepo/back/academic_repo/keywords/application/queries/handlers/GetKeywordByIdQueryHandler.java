package com.academicrepo.back.academic_repo.keywords.application.queries.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.keywords.application.queries.GetKeywordByIdQuery;
import com.academicrepo.back.academic_repo.keywords.domain.entities.DKeyword;
import com.academicrepo.back.academic_repo.keywords.domain.repositories.IKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetKeywordByIdQueryHandler {

    private final IKeywordRepository repository;

    public DKeyword execute(GetKeywordByIdQuery query) {
        try {
            DKeyword keyword = repository.findById(query.id());
            if (keyword == null) {
                throw new IllegalArgumentException(
                        "Palabra clave no encontrada con ID: " + query.id());
            }
            return keyword;
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
