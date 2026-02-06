package com.academicrepo.back.academic_repo.subcommunities.infrastructure.mappers;

import com.academicrepo.back.academic_repo.subcommunities.domain.entities.DSubcommunity;
import com.academicrepo.back.academic_repo.subcommunities.infrastructure.entities.Subcommunity;
import org.springframework.stereotype.Component;

@Component
public class SubcommunityMapper {

    public Subcommunity toPersistence(DSubcommunity domain) {
        if (domain == null) return null;

        Subcommunity.SubcommunityBuilder<?, ?> builder = Subcommunity.builder();
        builder.name(domain.getName());
        builder.description(domain.getDescription());
        builder.communityId(domain.getCommunityId());

        if (domain.getId() != null) builder.id(domain.getId());
        if (domain.getIsActive() != null) builder.isActive(domain.getIsActive());
        if (domain.getCreatedDate() != null) builder.createdDate(domain.getCreatedDate());
        if (domain.getUpdatedDate() != null) builder.updatedDate(domain.getUpdatedDate());

        return builder.build();
    }

    public DSubcommunity toDomain(Subcommunity entity) {
        if (entity == null) return null;

        DSubcommunity domain = new DSubcommunity();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setDescription(entity.getDescription());
        domain.setCommunityId(entity.getCommunityId());
        domain.setIsActive(entity.getIsActive());
        domain.setCreatedDate(entity.getCreatedDate());
        domain.setUpdatedDate(entity.getUpdatedDate());

        return domain;
    }
}
