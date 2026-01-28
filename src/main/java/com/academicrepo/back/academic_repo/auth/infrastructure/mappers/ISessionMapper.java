package com.academicrepo.back.academic_repo.auth.infrastructure.mappers;

import com.academicrepo.back.academic_repo.auth.domain.entities.DSession;
import com.academicrepo.back.academic_repo.auth.infrastructure.entities.Session;

public interface ISessionMapper {
    Session toPersistence(DSession domainSession);
    DSession toDomain(Session persistenceSession);
}
