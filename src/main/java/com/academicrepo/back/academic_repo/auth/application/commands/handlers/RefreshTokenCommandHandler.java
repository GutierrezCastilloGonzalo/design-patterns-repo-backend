package com.academicrepo.back.academic_repo.auth.application.commands.handlers;

import com.academicrepo.back.academic_repo.auth.application.commands.RefreshTokenCommand;
import com.academicrepo.back.academic_repo.auth.application.services.CustomUserDetailsService;
import com.academicrepo.back.academic_repo.auth.application.services.JwtService;
import com.academicrepo.back.academic_repo.auth.domain.entities.DAuthenticatedUser;
import com.academicrepo.back.academic_repo.auth.domain.entities.DSession;
import com.academicrepo.back.academic_repo.auth.domain.repositories.ISessionRepository;
import com.academicrepo.back.academic_repo.auth.presentation.dto.AuthResponseDto;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.users.domain.entities.DUser;
import com.academicrepo.back.academic_repo.users.domain.repositories.IUserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenCommandHandler {

    private final JwtService jwtService;
    private final ISessionRepository sessionRepository;
    private final IUserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;

    @Transactional
    public AuthResponseDto execute(RefreshTokenCommand command) {
        try {
            String refreshToken = command.refreshTokenDto().getRefreshToken();

            if (!jwtService.validateToken(refreshToken)
                    || !jwtService.isRefreshToken(refreshToken)) {
                throw new IllegalArgumentException("Refresh token inválido");
            }

            var sessionOpt = sessionRepository.findByRefreshToken(refreshToken);
            if (sessionOpt.isEmpty()) {
                throw new IllegalArgumentException("Sesión no encontrada o expirada");
            }

            DSession oldSession = sessionOpt.get();
            oldSession.setIsRevoked(true);
            sessionRepository.update(oldSession);

            String email = jwtService.extractUsername(refreshToken);
            Long userId = jwtService.extractUserId(refreshToken);

            DUser user = userRepository.findById(userId);
            if (user == null) {
                throw new IllegalArgumentException("Usuario no encontrado");
            }

            DAuthenticatedUser authUser =
                    (DAuthenticatedUser) userDetailsService.loadUserByUsername(email);

            String newAccessToken = jwtService.generateAccessToken(authUser, userId);
            String newRefreshToken = jwtService.generateRefreshToken(authUser, userId);

            DSession newSession = new DSession();
            newSession.setUserId(userId);
            newSession.setToken(newAccessToken);
            newSession.setRefreshToken(newRefreshToken);
            newSession.setExpiresAt(
                    LocalDateTime.now().plusSeconds(jwtService.getAccessTokenExpiration() / 1000));
            newSession.setRefreshExpiresAt(
                    LocalDateTime.now().plusSeconds(jwtService.getRefreshTokenExpiration() / 1000));
            newSession.setUserAgent(Optional.ofNullable(command.userAgent()));
            newSession.setIpAddress(Optional.ofNullable(command.ipAddress()));

            sessionRepository.save(newSession);

            return AuthResponseDto.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtService.getAccessTokenExpiration() / 1000)
                    .user(
                            AuthResponseDto.UserInfoDto.builder()
                                    .id(user.getId())
                                    .email(user.getEmail())
                                    .userName(user.getUserName().orElse(null))
                                    .roles(user.getRoleNames())
                                    .build())
                    .build();
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
