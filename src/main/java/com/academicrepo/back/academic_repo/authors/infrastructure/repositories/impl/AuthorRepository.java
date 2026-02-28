package com.academicrepo.back.academic_repo.authors.infrastructure.repositories.impl;

import com.academicrepo.back.academic_repo.authors.domain.entities.DAuthor;
import com.academicrepo.back.academic_repo.authors.domain.repositories.IAuthorRepository;
import com.academicrepo.back.academic_repo.authors.infrastructure.entities.Author;
import com.academicrepo.back.academic_repo.authors.infrastructure.mappers.AuthorMapper;
import com.academicrepo.back.academic_repo.authors.infrastructure.repositories.interfaces.IAuthorJpaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    public List<DAuthor> findAllByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        Map<Long, DAuthor> byId =
                jpaRepository.findByIdInAndIsActiveTrue(ids).stream()
                        .map(mapper::toDomain)
                        .collect(Collectors.toMap(DAuthor::getId, a -> a));
        return ids.stream().filter(byId::containsKey).map(byId::get).collect(Collectors.toList());
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
