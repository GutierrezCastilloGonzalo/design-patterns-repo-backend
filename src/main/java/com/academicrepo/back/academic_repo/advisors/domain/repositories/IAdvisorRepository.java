package com.academicrepo.back.academic_repo.advisors.domain.repositories;

import com.academicrepo.back.academic_repo.advisors.domain.entities.DAdvisor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAdvisorRepository {
    DAdvisor save(DAdvisor advisor);

    DAdvisor update(DAdvisor advisor);

    DAdvisor findById(Long id);

    Page<DAdvisor> findAll(Pageable pageConfig);

    boolean existsByEmail(String email);

    boolean existsByOrcid(String orcid);
}
