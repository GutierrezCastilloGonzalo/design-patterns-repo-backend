package com.academicrepo.back.academic_repo.keywords.domain.repositories;

import com.academicrepo.back.academic_repo.keywords.domain.entities.DKeyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IKeywordRepository {
    DKeyword save(DKeyword keyword);

    DKeyword update(DKeyword keyword);

    DKeyword findById(Long id);

    Page<DKeyword> findAll(Pageable pageConfig);

    boolean existsByWord(String word);
}
