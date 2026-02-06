package com.academicrepo.back.academic_repo.auth.domain.repositories;

import com.academicrepo.back.academic_repo.auth.domain.entities.DSession;
import java.util.List;
import java.util.Optional;

public interface ISessionRepository {
    DSession save(DSession session);

    DSession update(DSession session);

    Optional<DSession> findByToken(String token);

    Optional<DSession> findByRefreshToken(String refreshToken);

    List<DSession> findActiveSessionsByUserId(Long userId);

    void revokeAllUserSessions(Long userId);

    void deleteExpiredSessions();
}
