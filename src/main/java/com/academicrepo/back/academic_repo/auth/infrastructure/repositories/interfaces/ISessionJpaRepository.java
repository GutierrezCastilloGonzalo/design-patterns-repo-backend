package com.academicrepo.back.academic_repo.auth.infrastructure.repositories.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.academicrepo.back.academic_repo.auth.infrastructure.entities.Session;

@Repository
public interface ISessionJpaRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByTokenAndIsRevokedFalseAndIsActiveTrue(String token);

    Optional<Session> findByRefreshTokenAndIsRevokedFalseAndIsActiveTrue(String refreshToken);

    List<Session> findByUserIdAndIsRevokedFalseAndIsActiveTrue(Long userId);

    @Modifying
    @Query("UPDATE Session s SET s.isRevoked = true WHERE s.userId = :userId AND s.isRevoked = false")
    void revokeAllUserSessions(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM Session s WHERE s.expiresAt < :now AND s.refreshExpiresAt < :now")
    void deleteExpiredSessions(@Param("now") LocalDateTime now);
}
