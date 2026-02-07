package com.academicrepo.back.academic_repo.communities.infrastructure.repositories.impl;

import com.academicrepo.back.academic_repo.communities.domain.entities.DCommunity;
import com.academicrepo.back.academic_repo.communities.domain.repositories.ICommunityRepository;
import com.academicrepo.back.academic_repo.communities.infrastructure.entities.Community;
import com.academicrepo.back.academic_repo.communities.infrastructure.mappers.CommunityMapper;
import com.academicrepo.back.academic_repo.communities.infrastructure.repositories.interfaces.ICommunityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommunityRepository implements ICommunityRepository {

    private final ICommunityJpaRepository jpaRepository;
    private final CommunityMapper mapper;

    @Override
    public DCommunity save(DCommunity community) {
        Community entity = mapper.toPersistence(community);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DCommunity update(DCommunity community) {
        Community entity = mapper.toPersistence(community);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DCommunity findById(Long id) {
        return jpaRepository.findByIdAndIsActiveTrue(id).map(mapper::toDomain).orElse(null);
    }

    @Override
    public Page<DCommunity> findAll(Pageable pageConfig) {
        return jpaRepository.findByIsActiveTrue(pageConfig).map(mapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }
}
