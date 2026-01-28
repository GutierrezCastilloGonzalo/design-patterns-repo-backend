package com.academicrepo.back.academic_repo.auth.infrastructure.repositories.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.academicrepo.back.academic_repo.auth.domain.entities.DSession;
import com.academicrepo.back.academic_repo.auth.domain.repositories.ISessionRepository;
import com.academicrepo.back.academic_repo.auth.infrastructure.entities.Session;
import com.academicrepo.back.academic_repo.auth.infrastructure.mappers.SessionMapper;
import com.academicrepo.back.academic_repo.auth.infrastructure.repositories.interfaces.ISessionJpaRepository;

@Repository
public class SessionRepository implements ISessionRepository {

    private final ISessionJpaRepository jpaRepository;
    private final SessionMapper mapper;

    public SessionRepository(ISessionJpaRepository jpaRepository, SessionMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public DSession save(DSession session) {
        Session persistenceSession = mapper.toPersistence(session);
        persistenceSession = jpaRepository.save(persistenceSession);
        return mapper.toDomain(persistenceSession);
    }

    @Override
    public DSession update(DSession session) {
        Session persistenceSession = mapper.toPersistence(session);
        persistenceSession = jpaRepository.save(persistenceSession);
        return mapper.toDomain(persistenceSession);
    }

    @Override
    public Optional<DSession> findByToken(String token) {
        return jpaRepository.findByTokenAndIsRevokedFalseAndIsActiveTrue(token)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<DSession> findByRefreshToken(String refreshToken) {
        return jpaRepository.findByRefreshTokenAndIsRevokedFalseAndIsActiveTrue(refreshToken)
            .map(mapper::toDomain);
    }

    @Override
    public List<DSession> findActiveSessionsByUserId(Long userId) {
        return jpaRepository.findByUserIdAndIsRevokedFalseAndIsActiveTrue(userId)
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void revokeAllUserSessions(Long userId) {
        jpaRepository.revokeAllUserSessions(userId);
    }

    @Override
    public void deleteExpiredSessions() {
        jpaRepository.deleteExpiredSessions(LocalDateTime.now());
    }
}
