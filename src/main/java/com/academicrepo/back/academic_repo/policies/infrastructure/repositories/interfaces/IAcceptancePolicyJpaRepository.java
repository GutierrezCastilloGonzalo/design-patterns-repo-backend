package com.academicrepo.back.academic_repo.policies.infrastructure.repositories.interfaces;

import com.academicrepo.back.academic_repo.policies.infrastructure.entities.AcceptancePolicy;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAcceptancePolicyJpaRepository extends JpaRepository<AcceptancePolicy, Long> {
    Optional<AcceptancePolicy> findByIdAndIsActiveTrue(Long id);

    Page<AcceptancePolicy> findByIsActiveTrue(Pageable pageable);

    List<AcceptancePolicy> findByIsActiveTrue();

    List<AcceptancePolicy> findByIsActiveTrueAndRequiredTrue();

    boolean existsByCode(String code);
}
