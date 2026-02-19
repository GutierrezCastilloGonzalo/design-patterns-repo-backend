package com.academicrepo.back.academic_repo.licenses.domain.repositories;

import com.academicrepo.back.academic_repo.licenses.domain.entities.DLicense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ILicenseRepository {
    DLicense save(DLicense license);

    DLicense update(DLicense license);

    DLicense findById(Long id);

    Page<DLicense> findAll(Pageable pageConfig);

    boolean existsByName(String name);
}
