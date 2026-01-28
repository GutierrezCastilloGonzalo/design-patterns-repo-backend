package com.academicrepo.back.academic_repo.communities.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.academicrepo.back.academic_repo.communities.domain.entities.DCommunity;

public interface ICommunityRepository {
    DCommunity save(DCommunity community);
    DCommunity update(DCommunity community);
    DCommunity findById(Long id);
    Page<DCommunity> findAll(Pageable pageConfig);
    boolean existsByName(String name);
}
