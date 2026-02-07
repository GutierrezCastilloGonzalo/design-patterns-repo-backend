package com.academicrepo.back.academic_repo.auth.domain.entities;

import com.academicrepo.back.academic_repo.general.entities.domainEntities.BaseAbstractDomainEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DSession extends BaseAbstractDomainEntity {
    private Long userId;
    private String token;
    private String refreshToken;
    private LocalDateTime expiresAt;
    private LocalDateTime refreshExpiresAt;
    private Optional<String> userAgent = Optional.empty();
    private Optional<String> ipAddress = Optional.empty();
    private Boolean isRevoked = false;
}
