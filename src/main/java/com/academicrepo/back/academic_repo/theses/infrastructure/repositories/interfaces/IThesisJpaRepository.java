package com.academicrepo.back.academic_repo.theses.infrastructure.repositories.interfaces;

import com.academicrepo.back.academic_repo.theses.infrastructure.entities.Thesis;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IThesisJpaRepository extends JpaRepository<Thesis, Long> {
    Optional<Thesis> findByIdAndIsActiveTrue(Long id);

    Page<Thesis> findByIsActiveTrue(Pageable pageable);

    Page<Thesis> findByCollectionIdAndIsActiveTrue(Long collectionId, Pageable pageable);

    Page<Thesis> findByAdvisorIdAndIsActiveTrue(Long advisorId, Pageable pageable);

    boolean existsByTitleAndCollectionId(String title, Long collectionId);
}
