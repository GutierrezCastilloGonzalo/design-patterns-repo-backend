package com.academicrepo.back.academic_repo.keywords.domain.repositories;

import com.academicrepo.back.academic_repo.keywords.domain.entities.DKeyword;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IKeywordRepository {
    DKeyword save(DKeyword keyword);

    DKeyword update(DKeyword keyword);

    DKeyword findById(Long id);

    List<DKeyword> findAllByIds(List<Long> ids);

    Page<DKeyword> findAll(Pageable pageConfig);

    boolean existsByWord(String word);
}
