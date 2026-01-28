package com.academicrepo.back.academic_repo.authors.infrastructure.repositories.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.academicrepo.back.academic_repo.authors.infrastructure.entities.Author;

@Repository
public interface IAuthorJpaRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByIdAndIsActiveTrue(Long id);
    Page<Author> findByIsActiveTrue(Pageable pageable);
    boolean existsByEmail(String email);
    boolean existsByOrcid(String orcid);
}
