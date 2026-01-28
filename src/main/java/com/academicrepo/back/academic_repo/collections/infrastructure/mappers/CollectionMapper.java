package com.academicrepo.back.academic_repo.collections.infrastructure.mappers;

import org.springframework.stereotype.Component;

import com.academicrepo.back.academic_repo.collections.domain.entities.DCollection;
import com.academicrepo.back.academic_repo.collections.infrastructure.entities.Collection;

@Component
public class CollectionMapper {

    public Collection toPersistence(DCollection domain) {
        if (domain == null) return null;

        Collection.CollectionBuilder<?, ?> builder = Collection.builder();
        builder.name(domain.getName());
        builder.description(domain.getDescription());
        builder.subcommunityId(domain.getSubcommunityId());

        if (domain.getId() != null) builder.id(domain.getId());
        if (domain.getIsActive() != null) builder.isActive(domain.getIsActive());
        if (domain.getCreatedDate() != null) builder.createdDate(domain.getCreatedDate());
        if (domain.getUpdatedDate() != null) builder.updatedDate(domain.getUpdatedDate());

        return builder.build();
    }

    public DCollection toDomain(Collection entity) {
        if (entity == null) return null;

        DCollection domain = new DCollection();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setDescription(entity.getDescription());
        domain.setSubcommunityId(entity.getSubcommunityId());
        domain.setIsActive(entity.getIsActive());
        domain.setCreatedDate(entity.getCreatedDate());
        domain.setUpdatedDate(entity.getUpdatedDate());

        return domain;
    }
}
