package com.academicrepo.back.academic_repo.theses.infrastructure.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.academicrepo.back.academic_repo.theses.domain.entities.DThesis;
import com.academicrepo.back.academic_repo.theses.infrastructure.entities.KeyWordThesis;
import com.academicrepo.back.academic_repo.theses.infrastructure.entities.Thesis;
import com.academicrepo.back.academic_repo.theses.infrastructure.entities.ThesisAuthor;
import com.academicrepo.back.academic_repo.theses.infrastructure.repositories.interfaces.IKeyWordThesisJpaRepository;
import com.academicrepo.back.academic_repo.theses.infrastructure.repositories.interfaces.IThesisAuthorJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ThesisMapper {

    private final IThesisAuthorJpaRepository thesisAuthorRepository;
    private final IKeyWordThesisJpaRepository keyWordThesisRepository;

    public Thesis toPersistence(DThesis domain) {
        if (domain == null) return null;

        Thesis.ThesisBuilder<?, ?> builder = Thesis.builder();
        builder.title(domain.getTitle());
        builder.abstractText(domain.getAbstractText());
        builder.publicationDate(domain.getPublicationDate());
        builder.fileUrl(domain.getFileUrl());
        builder.numberOfPages(domain.getNumberOfPages());
        builder.language(domain.getLanguage());
        builder.documentType(domain.getDocumentType());
        builder.doi(domain.getDoi());
        builder.license(domain.getLicense());
        builder.collectionId(domain.getCollectionId());
        builder.advisorId(domain.getAdvisorId());

        if (domain.getId() != null) builder.id(domain.getId());
        if (domain.getIsActive() != null) builder.isActive(domain.getIsActive());
        if (domain.getCreatedDate() != null) builder.createdDate(domain.getCreatedDate());
        if (domain.getUpdatedDate() != null) builder.updatedDate(domain.getUpdatedDate());

        return builder.build();
    }

    public DThesis toDomain(Thesis entity) {
        if (entity == null) return null;

        DThesis domain = new DThesis();
        domain.setId(entity.getId());
        domain.setTitle(entity.getTitle());
        domain.setAbstractText(entity.getAbstractText());
        domain.setPublicationDate(entity.getPublicationDate());
        domain.setFileUrl(entity.getFileUrl());
        domain.setNumberOfPages(entity.getNumberOfPages());
        domain.setLanguage(entity.getLanguage());
        domain.setDocumentType(entity.getDocumentType());
        domain.setDoi(entity.getDoi());
        domain.setLicense(entity.getLicense());
        domain.setCollectionId(entity.getCollectionId());
        domain.setAdvisorId(entity.getAdvisorId());
        domain.setIsActive(entity.getIsActive());
        domain.setCreatedDate(entity.getCreatedDate());
        domain.setUpdatedDate(entity.getUpdatedDate());

        // Load author IDs
        List<ThesisAuthor> thesisAuthors = thesisAuthorRepository.findByThesisId(entity.getId());
        domain.setAuthorIds(thesisAuthors.stream()
            .map(ThesisAuthor::getAuthorId)
            .collect(Collectors.toList()));

        // Load keyword IDs
        List<KeyWordThesis> keyWordTheses = keyWordThesisRepository.findByThesisId(entity.getId());
        domain.setKeywordIds(keyWordTheses.stream()
            .map(KeyWordThesis::getKeywordId)
            .collect(Collectors.toList()));

        return domain;
    }
}
