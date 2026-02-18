package com.academicrepo.back.academic_repo.users.domain.repositories;

import com.academicrepo.back.academic_repo.users.domain.entities.DUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserRepository {
    DUser save(DUser user);

    DUser update(DUser user);

    DUser findById(Long id);

    DUser findByEmail(String email);

    DUser findByEmailIncludingInactive(String email);

    Page<DUser> findAll(Pageable pageConfig);

    Page<DUser> findAllIncludingInactive(Pageable pageConfig);

    DUser findByIdIncludingInactive(Long id);

    DUser deleteById(Long id);

    boolean existsByEmail(String email);
}
