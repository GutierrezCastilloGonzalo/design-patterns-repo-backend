package com.academicrepo.back.academic_repo.subcommunities.infrastructure.repositories.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.academicrepo.back.academic_repo.subcommunities.domain.entities.DSubcommunity;
import com.academicrepo.back.academic_repo.subcommunities.domain.repositories.ISubcommunityRepository;
import com.academicrepo.back.academic_repo.subcommunities.infrastructure.entities.Subcommunity;
import com.academicrepo.back.academic_repo.subcommunities.infrastructure.mappers.SubcommunityMapper;
import com.academicrepo.back.academic_repo.subcommunities.infrastructure.repositories.interfaces.ISubcommunityJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SubcommunityRepository implements ISubcommunityRepository {

    private final ISubcommunityJpaRepository jpaRepository;
    private final SubcommunityMapper mapper;

    @Override
    public DSubcommunity save(DSubcommunity subcommunity) {
        Subcommunity entity = mapper.toPersistence(subcommunity);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DSubcommunity update(DSubcommunity subcommunity) {
        Subcommunity entity = mapper.toPersistence(subcommunity);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DSubcommunity findById(Long id) {
        return jpaRepository.findByIdAndIsActiveTrue(id).map(mapper::toDomain).orElse(null);
    }

    @Override
    public Page<DSubcommunity> findAll(Pageable pageConfig) {
        return jpaRepository.findByIsActiveTrue(pageConfig).map(mapper::toDomain);
    }

    @Override
    public Page<DSubcommunity> findByCommunityId(Long communityId, Pageable pageConfig) {
        return jpaRepository.findByCommunityIdAndIsActiveTrue(communityId, pageConfig).map(mapper::toDomain);
    }

    @Override
    public boolean existsByNameAndCommunityId(String name, Long communityId) {
        return jpaRepository.existsByNameAndCommunityId(name, communityId);
    }
}
