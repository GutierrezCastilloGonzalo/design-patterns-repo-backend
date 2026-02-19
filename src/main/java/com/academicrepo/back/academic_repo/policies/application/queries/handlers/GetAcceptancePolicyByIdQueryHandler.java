package com.academicrepo.back.academic_repo.policies.application.queries.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.policies.application.queries.GetAcceptancePolicyByIdQuery;
import com.academicrepo.back.academic_repo.policies.domain.entities.DAcceptancePolicy;
import com.academicrepo.back.academic_repo.policies.domain.repositories.IAcceptancePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAcceptancePolicyByIdQueryHandler {

    private final IAcceptancePolicyRepository repository;

    public DAcceptancePolicy execute(GetAcceptancePolicyByIdQuery query) {
        try {
            DAcceptancePolicy policy = repository.findById(query.id());
            if (policy == null) {
                throw new IllegalArgumentException("Politica no encontrada con id: " + query.id());
            }
            return policy;
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
