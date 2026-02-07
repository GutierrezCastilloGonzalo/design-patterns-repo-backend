package com.academicrepo.back.academic_repo.auth.presentation.controllers;

import com.academicrepo.back.academic_repo.auth.application.commands.LoginCommand;
import com.academicrepo.back.academic_repo.auth.application.commands.LogoutCommand;
import com.academicrepo.back.academic_repo.auth.application.commands.RefreshTokenCommand;
import com.academicrepo.back.academic_repo.auth.application.commands.handlers.LoginCommandHandler;
import com.academicrepo.back.academic_repo.auth.application.commands.handlers.LogoutCommandHandler;
import com.academicrepo.back.academic_repo.auth.application.commands.handlers.RefreshTokenCommandHandler;
import com.academicrepo.back.academic_repo.auth.application.queries.GetCurrentUserQuery;
import com.academicrepo.back.academic_repo.auth.application.queries.handlers.GetCurrentUserQueryHandler;
import com.academicrepo.back.academic_repo.auth.domain.entities.DAuthenticatedUser;
import com.academicrepo.back.academic_repo.auth.presentation.dto.AuthResponseDto;
import com.academicrepo.back.academic_repo.auth.presentation.dto.LoginRequestDto;
import com.academicrepo.back.academic_repo.auth.presentation.dto.RefreshTokenRequestDto;
import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints de autenticación JWT")
public class AuthController extends BaseV1Controller {

    private final LoginCommandHandler loginCommandHandler;
    private final LogoutCommandHandler logoutCommandHandler;
    private final RefreshTokenCommandHandler refreshTokenCommandHandler;
    private final GetCurrentUserQueryHandler getCurrentUserQueryHandler;

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario y retorna tokens JWT")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequest, HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = getClientIpAddress(request);

        LoginCommand command = new LoginCommand(loginRequest, userAgent, ipAddress);
        AuthResponseDto response = loginCommandHandler.execute(command);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(
        summary = "Cerrar sesión",
        description = "Invalida el token de acceso actual",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7).trim();
        logoutCommandHandler.execute(new LogoutCommand(token));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refrescar token",
            description = "Genera nuevos tokens usando el refresh token")
    public ResponseEntity<AuthResponseDto> refreshToken(
            @Valid @RequestBody RefreshTokenRequestDto refreshRequest, HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = getClientIpAddress(request);

        RefreshTokenCommand command = new RefreshTokenCommand(refreshRequest, userAgent, ipAddress);
        AuthResponseDto response = refreshTokenCommandHandler.execute(command);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(
        summary = "Obtener usuario actual",
        description = "Retorna información del usuario autenticado",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<AuthResponseDto.UserInfoDto> getCurrentUser(
            @AuthenticationPrincipal DAuthenticatedUser authUser) {
        GetCurrentUserQuery query = new GetCurrentUserQuery(authUser.getId());
        AuthResponseDto.UserInfoDto userInfo = getCurrentUserQueryHandler.execute(query);
        return ResponseEntity.ok(userInfo);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
