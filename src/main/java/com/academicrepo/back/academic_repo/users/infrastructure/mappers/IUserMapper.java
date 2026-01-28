package com.academicrepo.back.academic_repo.users.infrastructure.mappers;

import com.academicrepo.back.academic_repo.users.domain.entities.DUser;
import com.academicrepo.back.academic_repo.users.infrastructure.entities.User;

public interface IUserMapper {
    User toPersistence(DUser domainUser);
    DUser toDomain(User persistenceUser);
}
