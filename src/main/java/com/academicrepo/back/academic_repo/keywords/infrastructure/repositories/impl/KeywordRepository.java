package com.academicrepo.back.academic_repo.keywords.infrastructure.repositories.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.academicrepo.back.academic_repo.keywords.domain.entities.DKeyword;
import com.academicrepo.back.academic_repo.keywords.domain.repositories.IKeywordRepository;
import com.academicrepo.back.academic_repo.keywords.infrastructure.entities.Keyword;
import com.academicrepo.back.academic_repo.keywords.infrastructure.mappers.KeywordMapper;
import com.academicrepo.back.academic_repo.keywords.infrastructure.repositories.interfaces.IKeywordJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class KeywordRepository implements IKeywordRepository {

    private final IKeywordJpaRepository jpaRepository;
    private final KeywordMapper mapper;

    @Override
    public DKeyword save(DKeyword keyword) {
        Keyword entity = mapper.toPersistence(keyword);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DKeyword update(DKeyword keyword) {
        Keyword entity = mapper.toPersistence(keyword);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DKeyword findById(Long id) {
        return jpaRepository.findByIdAndIsActiveTrue(id).map(mapper::toDomain).orElse(null);
    }

    @Override
    public Page<DKeyword> findAll(Pageable pageConfig) {
        return jpaRepository.findByIsActiveTrue(pageConfig).map(mapper::toDomain);
    }

    @Override
    public boolean existsByWord(String word) {
        return jpaRepository.existsByWord(word);
    }
}
