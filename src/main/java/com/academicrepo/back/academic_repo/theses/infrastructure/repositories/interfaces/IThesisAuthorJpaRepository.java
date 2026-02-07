package com.academicrepo.back.academic_repo.theses.infrastructure.repositories.interfaces;

import com.academicrepo.back.academic_repo.theses.infrastructure.entities.ThesisAuthor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IThesisAuthorJpaRepository extends JpaRepository<ThesisAuthor, Long> {
    List<ThesisAuthor> findByThesisId(Long thesisId);

    void deleteByThesisId(Long thesisId);
}
