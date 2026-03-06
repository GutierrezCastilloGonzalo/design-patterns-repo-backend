package com.academicrepo.back.academic_repo.collections.domain.repositories;

import com.academicrepo.back.academic_repo.collections.domain.entities.DCollection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICollectionRepository {
    DCollection save(DCollection collection);

    DCollection update(DCollection collection);

    DCollection findById(Long id);

    Page<DCollection> findAll(Pageable pageConfig);

    Page<DCollection> findBySubcommunityId(Long subcommunityId, Pageable pageConfig);

    boolean existsByNameAndSubcommunityId(String name, Long subcommunityId);

    List<DCollection> findAllActive();
}
