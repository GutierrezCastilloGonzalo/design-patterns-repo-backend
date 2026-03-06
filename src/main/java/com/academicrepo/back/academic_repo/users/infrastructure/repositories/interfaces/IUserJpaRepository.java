package com.academicrepo.back.academic_repo.users.infrastructure.repositories.interfaces;

import com.academicrepo.back.academic_repo.users.infrastructure.entities.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndIsActiveTrue(String email);

    Optional<User> findByIdAndIsActiveTrue(Long id);

    Page<User> findByIsActiveTrue(Pageable pageable);

    boolean existsByEmail(String email);

    long countByIsActiveTrue();
}
