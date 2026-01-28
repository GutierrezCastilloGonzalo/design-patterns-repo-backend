package com.academicrepo.back.academic_repo.advisors.infrastructure.repositories.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.academicrepo.back.academic_repo.advisors.domain.entities.DAdvisor;
import com.academicrepo.back.academic_repo.advisors.domain.repositories.IAdvisorRepository;
import com.academicrepo.back.academic_repo.advisors.infrastructure.entities.Advisor;
import com.academicrepo.back.academic_repo.advisors.infrastructure.mappers.AdvisorMapper;
import com.academicrepo.back.academic_repo.advisors.infrastructure.repositories.interfaces.IAdvisorJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdvisorRepository implements IAdvisorRepository {

    private final IAdvisorJpaRepository jpaRepository;
    private final AdvisorMapper mapper;

    @Override
    public DAdvisor save(DAdvisor advisor) {
        Advisor entity = mapper.toPersistence(advisor);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DAdvisor update(DAdvisor advisor) {
        Advisor entity = mapper.toPersistence(advisor);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DAdvisor findById(Long id) {
        return jpaRepository.findByIdAndIsActiveTrue(id).map(mapper::toDomain).orElse(null);
    }

    @Override
    public Page<DAdvisor> findAll(Pageable pageConfig) {
        return jpaRepository.findByIsActiveTrue(pageConfig).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByOrcid(String orcid) {
        return jpaRepository.existsByOrcid(orcid);
    }
}
