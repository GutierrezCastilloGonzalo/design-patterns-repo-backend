package com.academicrepo.back.academic_repo.policies.application.commands.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.policies.application.commands.CreateAcceptancePolicyCommand;
import com.academicrepo.back.academic_repo.policies.domain.entities.DAcceptancePolicy;
import com.academicrepo.back.academic_repo.policies.domain.repositories.IAcceptancePolicyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAcceptancePolicyCommandHandler {

    private final IAcceptancePolicyRepository repository;

    @Transactional
    public DAcceptancePolicy execute(CreateAcceptancePolicyCommand command) {
        try {
            if (repository.existsByCode(command.dto().getCode())) {
                throw new IllegalArgumentException(
                        "Ya existe una politica con el codigo: " + command.dto().getCode());
            }

            DAcceptancePolicy policy = new DAcceptancePolicy();
            policy.setCode(command.dto().getCode());
            policy.setName(command.dto().getName());
            policy.setDescription(command.dto().getDescription());
            policy.setRequired(
                    command.dto().getRequired() != null ? command.dto().getRequired() : false);
            policy.validate();

            return repository.save(policy);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
