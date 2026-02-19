package com.academicrepo.back.academic_repo.policies.infrastructure.repositories.impl;

import com.academicrepo.back.academic_repo.policies.domain.entities.DAcceptancePolicy;
import com.academicrepo.back.academic_repo.policies.domain.repositories.IAcceptancePolicyRepository;
import com.academicrepo.back.academic_repo.policies.infrastructure.entities.AcceptancePolicy;
import com.academicrepo.back.academic_repo.policies.infrastructure.mappers.AcceptancePolicyMapper;
import com.academicrepo.back.academic_repo.policies.infrastructure.repositories.interfaces.IAcceptancePolicyJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AcceptancePolicyRepository implements IAcceptancePolicyRepository {

    private final IAcceptancePolicyJpaRepository jpaRepository;
    private final AcceptancePolicyMapper mapper;

    @Override
    public DAcceptancePolicy save(DAcceptancePolicy policy) {
        AcceptancePolicy entity = mapper.toPersistence(policy);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DAcceptancePolicy update(DAcceptancePolicy policy) {
        AcceptancePolicy entity = mapper.toPersistence(policy);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DAcceptancePolicy findById(Long id) {
        return jpaRepository.findByIdAndIsActiveTrue(id).map(mapper::toDomain).orElse(null);
    }

    @Override
    public Page<DAcceptancePolicy> findAll(Pageable pageConfig) {
        return jpaRepository.findByIsActiveTrue(pageConfig).map(mapper::toDomain);
    }

    @Override
    public List<DAcceptancePolicy> findAllActive() {
        return jpaRepository.findByIsActiveTrue().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<DAcceptancePolicy> findRequired() {
        return jpaRepository.findByIsActiveTrueAndRequiredTrue().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
}
