package com.academicrepo.back.academic_repo.collections.application.queries.handlers;

import com.academicrepo.back.academic_repo.collections.application.queries.GetCollectionByIdQuery;
import com.academicrepo.back.academic_repo.collections.domain.entities.DCollection;
import com.academicrepo.back.academic_repo.collections.domain.repositories.ICollectionRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCollectionByIdQueryHandler {

    private final ICollectionRepository repository;

    public DCollection execute(GetCollectionByIdQuery query) {
        try {
            DCollection collection = repository.findById(query.id());
            if (collection == null) {
                throw new IllegalArgumentException("Coleccion no encontrada con ID: " + query.id());
            }
            return collection;
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
