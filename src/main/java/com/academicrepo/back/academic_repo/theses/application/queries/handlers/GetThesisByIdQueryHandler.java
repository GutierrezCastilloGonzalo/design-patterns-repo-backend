package com.academicrepo.back.academic_repo.theses.application.queries.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.theses.application.queries.GetThesisByIdQuery;
import com.academicrepo.back.academic_repo.theses.domain.entities.DThesis;
import com.academicrepo.back.academic_repo.theses.domain.repositories.IThesisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetThesisByIdQueryHandler {

    private final IThesisRepository repository;

    public DThesis execute(GetThesisByIdQuery query) {
        try {
            DThesis thesis = repository.findById(query.id());
            if (thesis == null) {
                throw new IllegalArgumentException("Tesis no encontrada con ID: " + query.id());
            }
            return thesis;
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
