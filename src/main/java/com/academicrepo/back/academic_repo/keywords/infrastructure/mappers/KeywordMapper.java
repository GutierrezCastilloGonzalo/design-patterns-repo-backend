package com.academicrepo.back.academic_repo.keywords.infrastructure.mappers;

import com.academicrepo.back.academic_repo.keywords.domain.entities.DKeyword;
import com.academicrepo.back.academic_repo.keywords.infrastructure.entities.Keyword;
import org.springframework.stereotype.Component;

@Component
public class KeywordMapper {

    public Keyword toPersistence(DKeyword domain) {
        if (domain == null) return null;

        Keyword.KeywordBuilder<?, ?> builder = Keyword.builder();
        builder.word(domain.getWord());

        if (domain.getId() != null) builder.id(domain.getId());
        if (domain.getIsActive() != null) builder.isActive(domain.getIsActive());
        if (domain.getCreatedDate() != null) builder.createdDate(domain.getCreatedDate());
        if (domain.getUpdatedDate() != null) builder.updatedDate(domain.getUpdatedDate());

        return builder.build();
    }

    public DKeyword toDomain(Keyword entity) {
        if (entity == null) return null;

        DKeyword domain = new DKeyword();
        domain.setId(entity.getId());
        domain.setWord(entity.getWord());
        domain.setIsActive(entity.getIsActive());
        domain.setCreatedDate(entity.getCreatedDate());
        domain.setUpdatedDate(entity.getUpdatedDate());

        return domain;
    }
}
