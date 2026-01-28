package com.academicrepo.back.academic_repo.collections.infrastructure.repositories.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.academicrepo.back.academic_repo.collections.domain.entities.DCollection;
import com.academicrepo.back.academic_repo.collections.domain.repositories.ICollectionRepository;
import com.academicrepo.back.academic_repo.collections.infrastructure.entities.Collection;
import com.academicrepo.back.academic_repo.collections.infrastructure.mappers.CollectionMapper;
import com.academicrepo.back.academic_repo.collections.infrastructure.repositories.interfaces.ICollectionJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CollectionRepository implements ICollectionRepository {

    private final ICollectionJpaRepository jpaRepository;
    private final CollectionMapper mapper;

    @Override
    public DCollection save(DCollection collection) {
        Collection entity = mapper.toPersistence(collection);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DCollection update(DCollection collection) {
        Collection entity = mapper.toPersistence(collection);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DCollection findById(Long id) {
        return jpaRepository.findByIdAndIsActiveTrue(id).map(mapper::toDomain).orElse(null);
    }

    @Override
    public Page<DCollection> findAll(Pageable pageConfig) {
        return jpaRepository.findByIsActiveTrue(pageConfig).map(mapper::toDomain);
    }

    @Override
    public Page<DCollection> findBySubcommunityId(Long subcommunityId, Pageable pageConfig) {
        return jpaRepository.findBySubcommunityIdAndIsActiveTrue(subcommunityId, pageConfig).map(mapper::toDomain);
    }

    @Override
    public boolean existsByNameAndSubcommunityId(String name, Long subcommunityId) {
        return jpaRepository.existsByNameAndSubcommunityId(name, subcommunityId);
    }
}
