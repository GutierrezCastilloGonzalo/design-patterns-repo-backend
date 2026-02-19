package com.academicrepo.back.academic_repo.licenses.infrastructure.repositories.interfaces;

import com.academicrepo.back.academic_repo.licenses.infrastructure.entities.License;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILicenseJpaRepository extends JpaRepository<License, Long> {
    Optional<License> findByIdAndIsActiveTrue(Long id);

    Page<License> findByIsActiveTrue(Pageable pageable);

    boolean existsByName(String name);
}
