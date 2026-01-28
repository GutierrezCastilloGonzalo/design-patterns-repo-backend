package com.academicrepo.back.academic_repo.advisors.infrastructure.repositories.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.academicrepo.back.academic_repo.advisors.infrastructure.entities.Advisor;

@Repository
public interface IAdvisorJpaRepository extends JpaRepository<Advisor, Long> {
    Optional<Advisor> findByIdAndIsActiveTrue(Long id);
    Page<Advisor> findByIsActiveTrue(Pageable pageable);
    boolean existsByEmail(String email);
    boolean existsByOrcid(String orcid);
}
