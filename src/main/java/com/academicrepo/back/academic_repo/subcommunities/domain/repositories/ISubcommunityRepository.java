package com.academicrepo.back.academic_repo.subcommunities.domain.repositories;

import com.academicrepo.back.academic_repo.subcommunities.domain.entities.DSubcommunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISubcommunityRepository {
    DSubcommunity save(DSubcommunity subcommunity);

    DSubcommunity update(DSubcommunity subcommunity);

    DSubcommunity findById(Long id);

    Page<DSubcommunity> findAll(Pageable pageConfig);

    Page<DSubcommunity> findByCommunityId(Long communityId, Pageable pageConfig);

    boolean existsByNameAndCommunityId(String name, Long communityId);
}
