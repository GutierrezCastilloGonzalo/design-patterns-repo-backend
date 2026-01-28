package com.academicrepo.back.academic_repo.theses.infrastructure.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.academicrepo.back.academic_repo.theses.infrastructure.entities.KeyWordThesis;

@Repository
public interface IKeyWordThesisJpaRepository extends JpaRepository<KeyWordThesis, Long> {
    List<KeyWordThesis> findByThesisId(Long thesisId);
    void deleteByThesisId(Long thesisId);
}
