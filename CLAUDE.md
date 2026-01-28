# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot 3.5.6 backend application for university academic document repository. Built with Java 21 using Spring Modulith + CQRS + Clean Architecture pattern (based on ptcagencydemo project architecture).

## Build & Development Commands

```bash
# Build the project
./mvnw clean package

# Run the application (port 8091)
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=AcademicRepoApplicationTests

# Start PostgreSQL database
docker-compose up -d

# Compile only (no tests)
./mvnw compile
```

On Windows, use `mvnw.cmd` instead of `./mvnw`.

## Architecture

### Package Structure

```
com.academicrepo.back.academic_repo/
├── config/          # Global configuration (CORS, OpenAPI)
├── general/         # Base entities, exceptions, utilities
├── auth/            # JWT authentication module
├── users/           # User management module
├── communities/     # Community module
├── subcommunities/  # Subcommunity module
├── collections/     # Collection module
├── theses/          # Thesis module (with M:M relations)
├── authors/         # Author module
├── advisors/        # Advisor module
└── keywords/        # Keyword module
```

### Module Structure (Clean Architecture + CQRS)

Each business module follows this structure:
```
[module]/
├── application/
│   ├── commands/handlers/    # Write operations (Create, Update, Deactivate)
│   └── queries/handlers/     # Read operations
├── domain/
│   ├── entities/             # Domain entities (D prefix: DThesis, DCommunity)
│   └── repositories/         # Repository interfaces (I prefix)
├── infrastructure/
│   ├── entities/             # JPA entities (@SuperBuilder)
│   ├── repositories/impl/    # Repository implementations
│   ├── repositories/interfaces/  # JPA repository interfaces
│   └── mappers/              # Domain ↔ Persistence mappers
└── presentation/
    ├── controllers/          # REST Controllers (extend BaseV1Controller)
    └── dto/                  # Data Transfer Objects
```

### Key Conventions

- **Domain entities**: Prefix `D` (e.g., `DThesis`, `DCommunity`)
- **Repository interfaces**: Prefix `I` (e.g., `IThesisRepository`)
- **JPA repositories**: Prefix `I` + suffix `JpaRepository` (e.g., `IThesisJpaRepository`)
- **Commands**: Record classes with suffix `Command` (e.g., `CreateThesisCommand`)
- **Handlers**: `@Service` classes with `@Transactional` from `jakarta.transaction`
- **Controllers**: Extend `BaseV1Controller`, routes start with `/v1/`
- **Base entity**: Use `@SuperBuilder(toBuilder = true)` for JPA entities

### Entity Hierarchy

```
Community (1:M) → Subcommunity (1:M) → Collection (1:M) → Thesis
Thesis (M:M) → Authors (via ThesisAuthor)
Thesis (M:M) → Keywords (via KeyWordThesis)
Thesis (M:1) → Advisor
```

## API Documentation

- Swagger UI: `http://localhost:8091/academic/api/swagger-ui.html`
- API Docs: `http://localhost:8091/academic/api/api-docs`

## Authentication

JWT-based authentication. Public endpoints:
- `POST /v1/auth/login` - Login
- `POST /v1/auth/refresh` - Refresh token
- `POST /v1/users` - User registration

Protected endpoints require `Authorization: Bearer <token>` header.

## Database

PostgreSQL with Flyway migrations. Connection details in `application-dev.properties`:
- URL: `jdbc:postgresql://localhost:5435/academic_repo`
- User: `academicuser`
- Password: `academicpass`
