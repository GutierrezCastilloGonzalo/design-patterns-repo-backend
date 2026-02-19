package com.academicrepo.back.academic_repo.policies.application.commands.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.policies.application.commands.UpdateAcceptancePolicyCommand;
import com.academicrepo.back.academic_repo.policies.domain.entities.DAcceptancePolicy;
import com.academicrepo.back.academic_repo.policies.domain.repositories.IAcceptancePolicyRepository;
import com.academicrepo.back.academic_repo.policies.presentation.dto.UpdateAcceptancePolicyDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateAcceptancePolicyCommandHandler {

    private final IAcceptancePolicyRepository repository;

    @Transactional
    public DAcceptancePolicy execute(UpdateAcceptancePolicyCommand command) {
        try {
            DAcceptancePolicy policy = repository.findById(command.id());
            if (policy == null) {
                throw new IllegalArgumentException(
                        "Politica no encontrada con id: " + command.id());
            }

            UpdateAcceptancePolicyDto dto = command.dto();

            if (dto.getCode() != null) policy.setCode(dto.getCode());
            if (dto.getName() != null) policy.setName(dto.getName());
            if (dto.getDescription() != null) policy.setDescription(dto.getDescription());
            if (dto.getRequired() != null) policy.setRequired(dto.getRequired());

            policy.validate();

            return repository.update(policy);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
