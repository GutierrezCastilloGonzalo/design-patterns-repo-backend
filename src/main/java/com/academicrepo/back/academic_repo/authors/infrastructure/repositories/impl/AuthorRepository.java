package com.academicrepo.back.academic_repo.authors.infrastructure.repositories.impl;

import com.academicrepo.back.academic_repo.authors.domain.entities.DAuthor;
import com.academicrepo.back.academic_repo.authors.domain.repositories.IAuthorRepository;
import com.academicrepo.back.academic_repo.authors.infrastructure.entities.Author;
import com.academicrepo.back.academic_repo.authors.infrastructure.mappers.AuthorMapper;
import com.academicrepo.back.academic_repo.authors.infrastructure.repositories.interfaces.IAuthorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthorRepository implements IAuthorRepository {

    private final IAuthorJpaRepository jpaRepository;
    private final AuthorMapper mapper;

    @Override
    public DAuthor save(DAuthor author) {
        Author entity = mapper.toPersistence(author);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DAuthor update(DAuthor author) {
        Author entity = mapper.toPersistence(author);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public DAuthor findById(Long id) {
        return jpaRepository.findByIdAndIsActiveTrue(id).map(mapper::toDomain).orElse(null);
    }

    @Override
    public Page<DAuthor> findAll(Pageable pageConfig) {
        return jpaRepository.findByIsActiveTrue(pageConfig).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByOrcid(String orcid) {
        return jpaRepository.existsByOrcid(orcid);
    }
}
