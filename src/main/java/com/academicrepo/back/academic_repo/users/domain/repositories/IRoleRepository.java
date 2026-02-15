package com.academicrepo.back.academic_repo.users.domain.repositories;

import com.academicrepo.back.academic_repo.users.domain.entities.DRole;
import java.util.List;
import java.util.Optional;

public interface IRoleRepository {
    Optional<DRole> findByName(String name);

    List<DRole> findAll();

    boolean existsByName(String name);

    DRole save(DRole role);
}
