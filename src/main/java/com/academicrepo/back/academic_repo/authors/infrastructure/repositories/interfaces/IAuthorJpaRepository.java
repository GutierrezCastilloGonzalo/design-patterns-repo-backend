package com.academicrepo.back.academic_repo.authors.infrastructure.repositories.interfaces;

import com.academicrepo.back.academic_repo.authors.infrastructure.entities.Author;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuthorJpaRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByIdAndIsActiveTrue(Long id);

    List<Author> findByIsActiveTrue();

    Page<Author> findByIsActiveTrue(Pageable pageable);

    List<Author> findByIdInAndIsActiveTrue(List<Long> ids);

    boolean existsByEmail(String email);

    boolean existsByOrcid(String orcid);
}
