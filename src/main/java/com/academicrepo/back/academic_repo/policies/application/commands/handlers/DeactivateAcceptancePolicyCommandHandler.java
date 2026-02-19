package com.academicrepo.back.academic_repo.policies.application.commands.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.policies.application.commands.DeactivateAcceptancePolicyCommand;
import com.academicrepo.back.academic_repo.policies.domain.entities.DAcceptancePolicy;
import com.academicrepo.back.academic_repo.policies.domain.repositories.IAcceptancePolicyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeactivateAcceptancePolicyCommandHandler {

    private final IAcceptancePolicyRepository repository;

    @Transactional
    public DAcceptancePolicy execute(DeactivateAcceptancePolicyCommand command) {
        try {
            DAcceptancePolicy policy = repository.findById(command.id());
            if (policy == null) {
                throw new IllegalArgumentException(
                        "Politica no encontrada con id: " + command.id());
            }

            policy.setIsActive(false);

            return repository.update(policy);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
