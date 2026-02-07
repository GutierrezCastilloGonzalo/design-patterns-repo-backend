package com.academicrepo.back.academic_repo.collections.infrastructure.repositories.interfaces;

import com.academicrepo.back.academic_repo.collections.infrastructure.entities.Collection;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICollectionJpaRepository extends JpaRepository<Collection, Long> {
    Optional<Collection> findByIdAndIsActiveTrue(Long id);

    Page<Collection> findByIsActiveTrue(Pageable pageable);

    Page<Collection> findBySubcommunityIdAndIsActiveTrue(Long subcommunityId, Pageable pageable);

    boolean existsByNameAndSubcommunityId(String name, Long subcommunityId);
}
