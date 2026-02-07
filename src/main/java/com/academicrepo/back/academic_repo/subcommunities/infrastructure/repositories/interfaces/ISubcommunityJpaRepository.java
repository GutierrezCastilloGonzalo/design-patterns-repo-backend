package com.academicrepo.back.academic_repo.subcommunities.infrastructure.repositories.interfaces;

import com.academicrepo.back.academic_repo.subcommunities.infrastructure.entities.Subcommunity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISubcommunityJpaRepository extends JpaRepository<Subcommunity, Long> {
    Optional<Subcommunity> findByIdAndIsActiveTrue(Long id);

    Page<Subcommunity> findByIsActiveTrue(Pageable pageable);

    Page<Subcommunity> findByCommunityIdAndIsActiveTrue(Long communityId, Pageable pageable);

    boolean existsByNameAndCommunityId(String name, Long communityId);
}
