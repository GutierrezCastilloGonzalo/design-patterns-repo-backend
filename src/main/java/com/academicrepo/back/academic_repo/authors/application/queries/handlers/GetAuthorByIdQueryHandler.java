package com.academicrepo.back.academic_repo.authors.application.queries.handlers;

import com.academicrepo.back.academic_repo.authors.application.queries.GetAuthorByIdQuery;
import com.academicrepo.back.academic_repo.authors.domain.entities.DAuthor;
import com.academicrepo.back.academic_repo.authors.domain.repositories.IAuthorRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAuthorByIdQueryHandler {

    private final IAuthorRepository repository;

    public DAuthor execute(GetAuthorByIdQuery query) {
        try {
            DAuthor author = repository.findById(query.id());
            if (author == null) {
                throw new IllegalArgumentException("Autor no encontrado con ID: " + query.id());
            }
            return author;
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
