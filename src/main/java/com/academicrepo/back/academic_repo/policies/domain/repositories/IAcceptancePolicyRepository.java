package com.academicrepo.back.academic_repo.policies.domain.repositories;

import com.academicrepo.back.academic_repo.policies.domain.entities.DAcceptancePolicy;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAcceptancePolicyRepository {
    DAcceptancePolicy save(DAcceptancePolicy policy);

    DAcceptancePolicy update(DAcceptancePolicy policy);

    DAcceptancePolicy findById(Long id);

    Page<DAcceptancePolicy> findAll(Pageable pageConfig);

    List<DAcceptancePolicy> findAllActive();

    List<DAcceptancePolicy> findRequired();

    boolean existsByCode(String code);
}
