package com.academicrepo.back.academic_repo.communities.infrastructure.mappers;

import com.academicrepo.back.academic_repo.communities.domain.entities.DCommunity;
import com.academicrepo.back.academic_repo.communities.infrastructure.entities.Community;
import org.springframework.stereotype.Component;

@Component
public class CommunityMapper {

    public Community toPersistence(DCommunity domain) {
        if (domain == null) return null;

        Community.CommunityBuilder<?, ?> builder = Community.builder();
        builder.name(domain.getName());
        builder.description(domain.getDescription());
        builder.logoUrl(domain.getLogoUrl());

        if (domain.getId() != null) builder.id(domain.getId());
        if (domain.getIsActive() != null) builder.isActive(domain.getIsActive());
        if (domain.getCreatedDate() != null) builder.createdDate(domain.getCreatedDate());
        if (domain.getUpdatedDate() != null) builder.updatedDate(domain.getUpdatedDate());

        return builder.build();
    }

    public DCommunity toDomain(Community entity) {
        if (entity == null) return null;

        DCommunity domain = new DCommunity();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setDescription(entity.getDescription());
        domain.setLogoUrl(entity.getLogoUrl());
        domain.setIsActive(entity.getIsActive());
        domain.setCreatedDate(entity.getCreatedDate());
        domain.setUpdatedDate(entity.getUpdatedDate());

        return domain;
    }
}
