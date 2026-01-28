package com.academicrepo.back.academic_repo.advisors.infrastructure.mappers;

import org.springframework.stereotype.Component;

import com.academicrepo.back.academic_repo.advisors.domain.entities.DAdvisor;
import com.academicrepo.back.academic_repo.advisors.infrastructure.entities.Advisor;

@Component
public class AdvisorMapper {

    public Advisor toPersistence(DAdvisor domain) {
        if (domain == null) return null;

        Advisor.AdvisorBuilder<?, ?> builder = Advisor.builder();
        builder.firstName(domain.getFirstName());
        builder.lastName(domain.getLastName());
        builder.email(domain.getEmail());
        builder.orcid(domain.getOrcid());
        builder.department(domain.getDepartment());
        builder.title(domain.getTitle());

        if (domain.getId() != null) builder.id(domain.getId());
        if (domain.getIsActive() != null) builder.isActive(domain.getIsActive());
        if (domain.getCreatedDate() != null) builder.createdDate(domain.getCreatedDate());
        if (domain.getUpdatedDate() != null) builder.updatedDate(domain.getUpdatedDate());

        return builder.build();
    }

    public DAdvisor toDomain(Advisor entity) {
        if (entity == null) return null;

        DAdvisor domain = new DAdvisor();
        domain.setId(entity.getId());
        domain.setFirstName(entity.getFirstName());
        domain.setLastName(entity.getLastName());
        domain.setEmail(entity.getEmail());
        domain.setOrcid(entity.getOrcid());
        domain.setDepartment(entity.getDepartment());
        domain.setTitle(entity.getTitle());
        domain.setIsActive(entity.getIsActive());
        domain.setCreatedDate(entity.getCreatedDate());
        domain.setUpdatedDate(entity.getUpdatedDate());

        return domain;
    }
}
