package com.academicrepo.back.academic_repo.users.infrastructure.repositories.interfaces;

import com.academicrepo.back.academic_repo.users.infrastructure.entities.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleJpaRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    boolean existsByName(String name);
}
