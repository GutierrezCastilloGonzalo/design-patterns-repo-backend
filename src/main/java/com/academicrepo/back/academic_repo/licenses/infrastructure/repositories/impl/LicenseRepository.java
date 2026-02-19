package com.academicrepo.back.academic_repo.licenses.infrastructure.repositories.impl;

import com.academicrepo.back.academic_repo.licenses.domain.entities.DLicense;
import com.academicrepo.back.academic_repo.licenses.domain.repositories.ILicenseRepository;
import com.academicrepo.back.academic_repo.licenses.infrastructure.entities.License;
import com.academicrepo.back.academic_repo.licenses.infrastructure.mappers.LicenseMapper;
import com.academicrepo.back.academic_repo.licenses.infrastructure.repositories.interfaces.ILicenseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LicenseRepository implements ILicenseRepository {

    private final ILicenseJpaRepository jpaRepository;
    private final LicenseMapper mapper;

    @Override
    public DLicense save(DLicense license) {
        License entity = mapper.toPersistence(license);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DLicense update(DLicense license) {
        License entity = mapper.toPersistence(license);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DLicense findById(Long id) {
        return jpaRepository.findByIdAndIsActiveTrue(id).map(mapper::toDomain).orElse(null);
    }

    @Override
    public Page<DLicense> findAll(Pageable pageConfig) {
        return jpaRepository.findByIsActiveTrue(pageConfig).map(mapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }
}
