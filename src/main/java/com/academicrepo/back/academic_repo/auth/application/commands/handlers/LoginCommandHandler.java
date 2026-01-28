package com.academicrepo.back.academic_repo.auth.application.commands.handlers;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.academicrepo.back.academic_repo.auth.application.commands.LoginCommand;
import com.academicrepo.back.academic_repo.auth.application.events.UserLoggedInEvent;
import com.academicrepo.back.academic_repo.auth.application.services.JwtService;
import com.academicrepo.back.academic_repo.auth.domain.entities.DAuthenticatedUser;
import com.academicrepo.back.academic_repo.auth.domain.entities.DSession;
import com.academicrepo.back.academic_repo.auth.domain.repositories.ISessionRepository;
import com.academicrepo.back.academic_repo.auth.presentation.dto.AuthResponseDto;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.users.domain.entities.DUser;
import com.academicrepo.back.academic_repo.users.domain.repositories.IUserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginCommandHandler {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final IUserRepository userRepository;
    private final ISessionRepository sessionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public AuthResponseDto execute(LoginCommand command) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    command.loginDto().getEmail(),
                    command.loginDto().getPassword()
                )
            );

            DAuthenticatedUser authUser = (DAuthenticatedUser) authentication.getPrincipal();
            DUser user = userRepository.findByEmail(authUser.getEmail());

            String accessToken = jwtService.generateAccessToken(authUser, user.getId());
            String refreshToken = jwtService.generateRefreshToken(authUser, user.getId());

            DSession session = new DSession();
            session.setUserId(user.getId());
            session.setToken(accessToken);
            session.setRefreshToken(refreshToken);
            session.setExpiresAt(LocalDateTime.now().plusSeconds(jwtService.getAccessTokenExpiration() / 1000));
            session.setRefreshExpiresAt(LocalDateTime.now().plusSeconds(jwtService.getRefreshTokenExpiration() / 1000));
            session.setUserAgent(Optional.ofNullable(command.userAgent()));
            session.setIpAddress(Optional.ofNullable(command.ipAddress()));

            sessionRepository.save(session);

            eventPublisher.publishEvent(new UserLoggedInEvent(user.getId(), user.getEmail(), command.ipAddress()));

            return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpiration() / 1000)
                .user(AuthResponseDto.UserInfoDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .userName(user.getUserName().orElse(null))
                    .build())
                .build();
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
