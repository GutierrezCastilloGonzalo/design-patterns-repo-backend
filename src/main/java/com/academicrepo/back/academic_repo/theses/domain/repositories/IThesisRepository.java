package com.academicrepo.back.academic_repo.theses.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.academicrepo.back.academic_repo.theses.domain.entities.DThesis;

public interface IThesisRepository {
    DThesis save(DThesis thesis);
    DThesis update(DThesis thesis);
    DThesis findById(Long id);
    Page<DThesis> findAll(Pageable pageConfig);
    Page<DThesis> findByCollectionId(Long collectionId, Pageable pageConfig);
    Page<DThesis> findByAdvisorId(Long advisorId, Pageable pageConfig);
    boolean existsByTitleAndCollectionId(String title, Long collectionId);
}
