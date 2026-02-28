package com.academicrepo.back.academic_repo.authors.domain.repositories;

import com.academicrepo.back.academic_repo.authors.domain.entities.DAuthor;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAuthorRepository {
    DAuthor save(DAuthor author);

    DAuthor update(DAuthor author);

    DAuthor findById(Long id);

    List<DAuthor> findAllByIds(List<Long> ids);

    Page<DAuthor> findAll(Pageable pageConfig);

    boolean existsByEmail(String email);

    boolean existsByOrcid(String orcid);
}
