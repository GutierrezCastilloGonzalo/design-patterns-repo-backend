package com.academicrepo.back.academic_repo.auth.infrastructure.mappers;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.academicrepo.back.academic_repo.auth.domain.entities.DSession;
import com.academicrepo.back.academic_repo.auth.infrastructure.entities.Session;

@Component
public class SessionMapper implements ISessionMapper {

    @Override
    public Session toPersistence(DSession domainSession) {
        if (domainSession == null) {
            return null;
        }

        Session.SessionBuilder<?, ?> builder = Session.builder();
        builder.userId(domainSession.getUserId());
        builder.token(domainSession.getToken());
        builder.refreshToken(domainSession.getRefreshToken());
        builder.expiresAt(domainSession.getExpiresAt());
        builder.refreshExpiresAt(domainSession.getRefreshExpiresAt());
        builder.userAgent(domainSession.getUserAgent() != null ? domainSession.getUserAgent().orElse(null) : null);
        builder.ipAddress(domainSession.getIpAddress() != null ? domainSession.getIpAddress().orElse(null) : null);
        builder.isRevoked(domainSession.getIsRevoked());

        if (domainSession.getId() != null) {
            builder.id(domainSession.getId());
        }
        if (domainSession.getIsActive() != null) {
            builder.isActive(domainSession.getIsActive());
        }
        if (domainSession.getCreatedDate() != null) {
            builder.createdDate(domainSession.getCreatedDate());
        }
        if (domainSession.getUpdatedDate() != null) {
            builder.updatedDate(domainSession.getUpdatedDate());
        }

        return builder.build();
    }

    @Override
    public DSession toDomain(Session persistenceSession) {
        if (persistenceSession == null) {
            return null;
        }

        DSession domainSession = new DSession();
        domainSession.setId(persistenceSession.getId());
        domainSession.setUserId(persistenceSession.getUserId());
        domainSession.setToken(persistenceSession.getToken());
        domainSession.setRefreshToken(persistenceSession.getRefreshToken());
        domainSession.setExpiresAt(persistenceSession.getExpiresAt());
        domainSession.setRefreshExpiresAt(persistenceSession.getRefreshExpiresAt());
        domainSession.setUserAgent(Optional.ofNullable(persistenceSession.getUserAgent()));
        domainSession.setIpAddress(Optional.ofNullable(persistenceSession.getIpAddress()));
        domainSession.setIsRevoked(persistenceSession.getIsRevoked());
        domainSession.setIsActive(persistenceSession.getIsActive());
        domainSession.setCreatedDate(persistenceSession.getCreatedDate());
        domainSession.setUpdatedDate(persistenceSession.getUpdatedDate());

        return domainSession;
    }
}
