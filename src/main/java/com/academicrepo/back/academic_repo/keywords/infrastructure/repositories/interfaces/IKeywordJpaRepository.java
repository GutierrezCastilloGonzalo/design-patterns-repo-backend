package com.academicrepo.back.academic_repo.keywords.infrastructure.repositories.interfaces;

import com.academicrepo.back.academic_repo.keywords.infrastructure.entities.Keyword;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IKeywordJpaRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByIdAndIsActiveTrue(Long id);

    Page<Keyword> findByIsActiveTrue(Pageable pageable);

    boolean existsByWord(String word);
}
