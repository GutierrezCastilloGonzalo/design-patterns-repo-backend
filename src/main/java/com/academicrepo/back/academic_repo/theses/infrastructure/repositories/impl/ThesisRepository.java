package com.academicrepo.back.academic_repo.theses.infrastructure.repositories.impl;

import com.academicrepo.back.academic_repo.theses.domain.entities.DThesis;
import com.academicrepo.back.academic_repo.theses.domain.repositories.IThesisRepository;
import com.academicrepo.back.academic_repo.theses.infrastructure.entities.KeyWordThesis;
import com.academicrepo.back.academic_repo.theses.infrastructure.entities.Thesis;
import com.academicrepo.back.academic_repo.theses.infrastructure.entities.ThesisAuthor;
import com.academicrepo.back.academic_repo.theses.infrastructure.mappers.ThesisMapper;
import com.academicrepo.back.academic_repo.theses.infrastructure.repositories.interfaces.IKeyWordThesisJpaRepository;
import com.academicrepo.back.academic_repo.theses.infrastructure.repositories.interfaces.IThesisAuthorJpaRepository;
import com.academicrepo.back.academic_repo.theses.infrastructure.repositories.interfaces.IThesisJpaRepository;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ThesisRepository implements IThesisRepository {

    private final IThesisJpaRepository jpaRepository;
    private final IThesisAuthorJpaRepository thesisAuthorRepository;
    private final IKeyWordThesisJpaRepository keyWordThesisRepository;
    private final ThesisMapper mapper;

    @Override
    public DThesis save(DThesis thesis) {
        Thesis entity = mapper.toPersistence(thesis);
        entity = jpaRepository.save(entity);

        saveRelations(entity.getId(), thesis.getAuthorIds(), thesis.getKeywordIds());

        return mapper.toDomain(entity);
    }

    @Override
    public DThesis update(DThesis thesis) {
        Thesis entity = mapper.toPersistence(thesis);
        entity = jpaRepository.save(entity);

        // Clear and re-save relations
        thesisAuthorRepository.deleteByThesisId(entity.getId());
        keyWordThesisRepository.deleteByThesisId(entity.getId());
        saveRelations(entity.getId(), thesis.getAuthorIds(), thesis.getKeywordIds());

        return mapper.toDomain(entity);
    }

    private void saveRelations(Long thesisId, List<Long> authorIds, List<Long> keywordIds) {
        if (authorIds != null && !authorIds.isEmpty()) {
            AtomicInteger order = new AtomicInteger(1);
            authorIds.forEach(
                    authorId -> {
                        ThesisAuthor ta =
                                ThesisAuthor.builder()
                                        .thesisId(thesisId)
                                        .authorId(authorId)
                                        .orderNumber(order.getAndIncrement())
                                        .build();
                        thesisAuthorRepository.save(ta);
                    });
        }

        if (keywordIds != null && !keywordIds.isEmpty()) {
            keywordIds.forEach(
                    keywordId -> {
                        KeyWordThesis kwt =
                                KeyWordThesis.builder()
                                        .thesisId(thesisId)
                                        .keywordId(keywordId)
                                        .build();
                        keyWordThesisRepository.save(kwt);
                    });
        }
    }

    @Override
    public DThesis findById(Long id) {
        return jpaRepository.findByIdAndIsActiveTrue(id).map(mapper::toDomain).orElse(null);
    }

    @Override
    public Page<DThesis> findAll(Pageable pageConfig) {
        return jpaRepository.findByIsActiveTrue(pageConfig).map(mapper::toDomain);
    }

    @Override
    public Page<DThesis> findByCollectionId(Long collectionId, Pageable pageConfig) {
        return jpaRepository
                .findByCollectionIdAndIsActiveTrue(collectionId, pageConfig)
                .map(mapper::toDomain);
    }

    @Override
    public Page<DThesis> findByAdvisorId(Long advisorId, Pageable pageConfig) {
        return jpaRepository
                .findByAdvisorIdAndIsActiveTrue(advisorId, pageConfig)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByTitleAndCollectionId(String title, Long collectionId) {
        return jpaRepository.existsByTitleAndCollectionId(title, collectionId);
    }
}
