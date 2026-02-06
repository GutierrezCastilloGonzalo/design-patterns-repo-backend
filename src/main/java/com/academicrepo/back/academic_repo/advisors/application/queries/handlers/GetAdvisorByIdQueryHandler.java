package com.academicrepo.back.academic_repo.advisors.application.queries.handlers;

import com.academicrepo.back.academic_repo.advisors.application.queries.GetAdvisorByIdQuery;
import com.academicrepo.back.academic_repo.advisors.domain.entities.DAdvisor;
import com.academicrepo.back.academic_repo.advisors.domain.repositories.IAdvisorRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAdvisorByIdQueryHandler {

    private final IAdvisorRepository repository;

    public DAdvisor execute(GetAdvisorByIdQuery query) {
        try {
            DAdvisor advisor = repository.findById(query.id());
            if (advisor == null) {
                throw new IllegalArgumentException("Asesor no encontrado con ID: " + query.id());
            }
            return advisor;
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
