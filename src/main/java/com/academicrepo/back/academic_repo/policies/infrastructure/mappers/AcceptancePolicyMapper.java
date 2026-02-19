package com.academicrepo.back.academic_repo.policies.infrastructure.mappers;

import com.academicrepo.back.academic_repo.policies.domain.entities.DAcceptancePolicy;
import com.academicrepo.back.academic_repo.policies.infrastructure.entities.AcceptancePolicy;
import org.springframework.stereotype.Component;

@Component
public class AcceptancePolicyMapper {

    public AcceptancePolicy toPersistence(DAcceptancePolicy domain) {
        if (domain == null) return null;

        AcceptancePolicy.AcceptancePolicyBuilder<?, ?> builder = AcceptancePolicy.builder();
        builder.code(domain.getCode())
                .name(domain.getName())
                .description(domain.getDescription())
                .required(domain.getRequired() != null ? domain.getRequired() : false);

        if (domain.getId() != null) builder.id(domain.getId());
        if (domain.getIsActive() != null) builder.isActive(domain.getIsActive());
        if (domain.getCreatedDate() != null) builder.createdDate(domain.getCreatedDate());
        if (domain.getUpdatedDate() != null) builder.updatedDate(domain.getUpdatedDate());

        return builder.build();
    }

    public DAcceptancePolicy toDomain(AcceptancePolicy entity) {
        if (entity == null) return null;

        DAcceptancePolicy domain = new DAcceptancePolicy();
        domain.setId(entity.getId());
        domain.setCode(entity.getCode());
        domain.setName(entity.getName());
        domain.setDescription(entity.getDescription());
        domain.setRequired(entity.getRequired());
        domain.setIsActive(entity.getIsActive());
        domain.setCreatedDate(entity.getCreatedDate());
        domain.setUpdatedDate(entity.getUpdatedDate());

        return domain;
    }
}
