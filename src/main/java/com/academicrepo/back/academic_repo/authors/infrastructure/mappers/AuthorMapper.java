package com.academicrepo.back.academic_repo.authors.infrastructure.mappers;

import com.academicrepo.back.academic_repo.authors.domain.entities.DAuthor;
import com.academicrepo.back.academic_repo.authors.infrastructure.entities.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public Author toPersistence(DAuthor domain) {
        if (domain == null) return null;

        Author.AuthorBuilder<?, ?> builder = Author.builder();
        builder.firstName(domain.getFirstName());
        builder.lastName(domain.getLastName());
        builder.email(domain.getEmail());
        builder.orcid(domain.getOrcid());
        builder.affiliation(domain.getAffiliation());

        if (domain.getId() != null) builder.id(domain.getId());
        if (domain.getIsActive() != null) builder.isActive(domain.getIsActive());
        if (domain.getCreatedDate() != null) builder.createdDate(domain.getCreatedDate());
        if (domain.getUpdatedDate() != null) builder.updatedDate(domain.getUpdatedDate());

        return builder.build();
    }

    public DAuthor toDomain(Author entity) {
        if (entity == null) return null;

        DAuthor domain = new DAuthor();
        domain.setId(entity.getId());
        domain.setFirstName(entity.getFirstName());
        domain.setLastName(entity.getLastName());
        domain.setEmail(entity.getEmail());
        domain.setOrcid(entity.getOrcid());
        domain.setAffiliation(entity.getAffiliation());
        domain.setIsActive(entity.getIsActive());
        domain.setCreatedDate(entity.getCreatedDate());
        domain.setUpdatedDate(entity.getUpdatedDate());

        return domain;
    }
}
