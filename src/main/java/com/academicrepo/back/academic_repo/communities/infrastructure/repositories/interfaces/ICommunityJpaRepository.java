package com.academicrepo.back.academic_repo.communities.infrastructure.repositories.interfaces;

import com.academicrepo.back.academic_repo.communities.infrastructure.entities.Community;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommunityJpaRepository extends JpaRepository<Community, Long> {
    Optional<Community> findByIdAndIsActiveTrue(Long id);

    Page<Community> findByIsActiveTrue(Pageable pageable);

    boolean existsByName(String name);
}
