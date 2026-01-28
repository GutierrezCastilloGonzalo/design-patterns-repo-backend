package com.academicrepo.back.academic_repo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "Academic Repository API",
        version = "1.0.0",
        description = "API REST para el sistema de repositorio de documentos académicos universitarios. " +
                      "Incluye autenticación JWT, gestión de comunidades, colecciones, tesis, autores y asesores.",
        contact = @Contact(
            name = "Academic Repository Team",
            email = "support@academicrepo.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(url = "/academic/api", description = "API Server")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Token de autenticación JWT. Obtén el token del endpoint /v1/auth/login"
)
public class OpenApiConfig {
}
