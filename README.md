# Academic Repository Backend

Backend REST API para un repositorio de documentos academicos universitarios. Construido con **Spring Boot 3.5.6** y **Java 21**, aplicando **Spring Modulith**, **CQRS** y **Clean Architecture**.

## Tech Stack

| Categoria | Tecnologia |
|-----------|------------|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.5.6 |
| Arquitectura modular | Spring Modulith 1.4.3 |
| Seguridad | Spring Security 6 + JWT (jjwt 0.12.6) |
| Base de datos | PostgreSQL |
| ORM | Spring Data JPA + Hibernate |
| Migraciones | Liquibase 4.31.1 |
| Consultas tipadas | QueryDSL 5.1.0 |
| Mapeo de objetos | MapStruct 1.6.3 |
| Documentacion API | SpringDoc OpenAPI 2.8.13 (Swagger UI + Scalar) |
| Contenedores | Docker Compose |
| Formato de codigo | Spotless + google-java-format (AOSP) |
| Boilerplate | Lombok |

---

## Arquitectura

El proyecto implementa una arquitectura **Clean Architecture + CQRS** dentro de un **monolito modular** gestionado por Spring Modulith.

### Diagrama de capas

```
┌─────────────────────────────────────────────────┐
│                 Presentation                    │
│         Controllers + DTOs (/v1/...)            │
├─────────────────────────────────────────────────┤
│                 Application                     │
│    Commands/Handlers    │   Queries/Handlers     │
│      (escritura)        │     (lectura)          │
├─────────────────────────────────────────────────┤
│                   Domain                        │
│       Entidades (D*)  +  Repositorios (I*)      │
├─────────────────────────────────────────────────┤
│               Infrastructure                    │
│   JPA Entities + Repos Impl + Mappers           │
└─────────────────────────────────────────────────┘
```

### Estructura de modulos

```
com.academicrepo.back.academic_repo/
├── config/            # Configuracion global (CORS, OpenAPI, DataSeeder)
├── general/           # Entidades base, excepciones, utilidades
├── auth/              # Modulo de autenticacion JWT
├── users/             # Gestion de usuarios
├── communities/       # Comunidades
├── subcommunities/    # Subcomunidades
├── collections/       # Colecciones
├── theses/            # Tesis (relaciones M:M)
├── authors/           # Autores
├── advisors/          # Asesores
└── keywords/          # Palabras clave
```

Cada modulo de negocio sigue esta estructura interna:

```
[modulo]/
├── application/
│   ├── commands/handlers/     # Operaciones de escritura (Create, Update, Deactivate)
│   └── queries/handlers/      # Operaciones de lectura
├── domain/
│   ├── entities/              # Entidades de dominio (prefijo D)
│   └── repositories/          # Interfaces de repositorio (prefijo I)
├── infrastructure/
│   ├── entities/              # Entidades JPA (@SuperBuilder)
│   ├── repositories/impl/     # Implementaciones de repositorio
│   ├── repositories/interfaces/  # Interfaces JPA
│   └── mappers/               # Mappers dominio <-> persistencia
└── presentation/
    ├── controllers/           # REST Controllers (extienden BaseV1Controller)
    └── dto/                   # Data Transfer Objects
```

### Jerarquia de entidades

```
Community (1:M) ──> Subcommunity (1:M) ──> Collection (1:M) ──> Thesis
                                                                  ├── (M:1) Advisor
                                                                  ├── (M:M) Authors    [via ThesisAuthor]
                                                                  └── (M:M) Keywords   [via KeyWordThesis]
```

---

## Patrones de diseno

### Patrones arquitectonicos

| Patron | Implementacion |
|--------|----------------|
| **Clean Architecture** | Separacion estricta en 4 capas: `presentation`, `application`, `domain`, `infrastructure`. Las dependencias apuntan hacia el dominio. |
| **CQRS** | Segregacion de comandos (27 commands) y consultas (10 queries) con handlers independientes. Los comandos mutan estado con `@Transactional`; las consultas solo leen. |
| **Modular Monolith** | Spring Modulith con `@ApplicationModule` en cada modulo. Define dependencias explicitas entre modulos y refuerza limites de acoplamiento. |
| **Domain-Driven Design** | Entidades de dominio con lenguaje ubicuo (`DThesis`, `DCommunity`), repositorios de dominio como abstracciones, y separacion entre modelo de dominio y modelo de persistencia. |

### Patrones estructurales

| Patron | Implementacion |
|--------|----------------|
| **Repository** | Doble capa: interfaces de dominio (`IThesisRepository`) implementadas en infraestructura, que delegan a repositorios JPA (`IThesisJpaRepository`). Desacopla el dominio de la tecnologia de persistencia. |
| **Mapper** | MapStruct para conversion bidireccional entre entidades de dominio (`DThesis`) y entidades JPA (`Thesis`). Elimina mapeo manual y garantiza type-safety. |
| **DTO (Data Transfer Object)** | 20+ DTOs en la capa de presentacion para desacoplar la API publica del modelo interno. |
| **Builder** | `@SuperBuilder(toBuilder = true)` en todas las entidades JPA via Lombok. Permite construccion fluida e inmutable de objetos. |
| **MappedSuperclass (herencia)** | `BaseAbstractEntity` como clase base JPA con campos comunes (`id`, `isActive`, `createdDate`, `updatedDate`). Todas las entidades heredan de esta. |

### Patrones de comportamiento

| Patron | Implementacion |
|--------|----------------|
| **Observer / Event-Driven** | Publicacion de eventos de dominio (`UserLoggedInEvent`, `UserCreatedDomainEvent`) via `ApplicationEventPublisher`. Permite comunicacion entre modulos sin acoplamiento directo. |
| **Strategy** | `HttpExceptionUtils` mapea distintos tipos de excepciones a codigos HTTP apropiados (400, 401, 403, 404, 409, 500, 504), seleccionando la estrategia de respuesta segun el tipo de error. |
| **Template Method** | Clases base (`BaseAbstractDomainEntity`, `BaseAbstractEntity`) definen la estructura comun; los modulos concretos extienden con campos especificos. |
| **Chain of Responsibility** | Pipeline de filtros de Spring Security: `CorsFilter` -> `JwtAuthenticationFilter` -> `UsernamePasswordAuthenticationFilter`. Cada filtro procesa o delega la peticion. |

### Patrones adicionales

| Patron | Implementacion |
|--------|----------------|
| **Dependency Injection** | Inyeccion de dependencias via constructor en todos los `@Service` y `@RestController`, gestionado por el contenedor IoC de Spring. |
| **Singleton** | Beans de Spring (`@Service`, `@Component`, `@Configuration`) son singletons por defecto dentro del contexto de la aplicacion. |
| **Proxy** | Spring genera proxies dinamicos para manejar `@Transactional`, seguridad (`@PreAuthorize`), y otros aspectos transversales de forma transparente. |
| **Decorator** | `@EntityListeners(AuditingEntityListener.class)` decora las entidades JPA para inyectar automaticamente timestamps de auditoria (`@CreatedDate`, `@LastModifiedDate`). |
| **Factory Method** | Creacion de tokens JWT y sesiones de autenticacion en los command handlers, encapsulando la logica de construccion. |

---

## Autenticacion (JWT)

Autenticacion stateless basada en JSON Web Tokens.

| Propiedad | Valor |
|-----------|-------|
| Access Token | 15 minutos |
| Refresh Token | 7 dias |
| Algoritmo | HMAC-SHA256 |
| Encoding de passwords | BCrypt |
| Gestion de sesiones | Stateless (sin sesion HTTP) |

### Endpoints publicos

```
POST /v1/auth/login      # Iniciar sesion
POST /v1/auth/refresh     # Refrescar token
POST /v1/users            # Registro de usuario
```

### Flujo de autenticacion

```
1. Cliente envia credenciales ──> POST /v1/auth/login
2. Servidor valida credenciales via AuthenticationManager
3. Genera access token + refresh token
4. Almacena sesion en BD (tracking de IP y User-Agent)
5. Publica UserLoggedInEvent
6. Retorna tokens al cliente

Peticiones protegidas:
   Authorization: Bearer <access_token>
   ──> JwtAuthenticationFilter valida token
   ──> Verifica sesion en BD (no revocada)
   ──> Carga UserDetails y establece SecurityContext
```

---

## Documentacion de la API

| Recurso | URL |
|---------|-----|
| Swagger UI | `http://localhost:8091/academic/api/swagger-ui.html` |
| Scalar UI | `http://localhost:8091/academic/api/scalar.html` |
| OpenAPI JSON | `http://localhost:8091/academic/api/api-docs` |

Todas las rutas de la API siguen el patron `/v1/{recurso}` con versionado por path.

---

## Primeros pasos

### Prerrequisitos

- Java 21+
- Docker y Docker Compose
- Maven (incluido via wrapper `mvnw`)

### 1. Clonar el repositorio

```bash
git clone https://github.com/GutierrezCastilloGonzalo/design-patterns-repo-backend.git
cd design-patterns-repo-backend
```

### 2. Iniciar la base de datos

```bash
docker-compose up -d
```

PostgreSQL estara disponible en `localhost:5435` con la base de datos `academic_repo`.

### 3. Ejecutar la aplicacion

```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

La aplicacion inicia en `http://localhost:8091/academic/api`.

### Usuario inicial (seed)

| Campo | Valor |
|-------|-------|
| Email | `superadmin@academicrepo.com` |
| Username | `superadmin` |
| Password | `SuperAdmin123!` |

---

## Comandos utiles

```bash
# Compilar el proyecto
mvnw.cmd clean package

# Ejecutar tests
mvnw.cmd test

# Ejecutar un test especifico
mvnw.cmd test -Dtest=AcademicRepoApplicationTests

# Compilar sin tests
mvnw.cmd compile

# Formatear codigo (Spotless + google-java-format AOSP)
mvnw.cmd spotless:apply

# Verificar formato sin modificar
mvnw.cmd spotless:check

# Generar changelog de Liquibase desde la BD
mvnw.cmd liquibase:generateChangeLog

# Generar diff entre entidades JPA y BD
mvnw.cmd liquibase:diff
```

---

## Convenciones de codigo

| Elemento | Convencion | Ejemplo |
|----------|------------|---------|
| Entidades de dominio | Prefijo `D` | `DThesis`, `DCommunity` |
| Interfaces de repositorio | Prefijo `I` | `IThesisRepository` |
| Repositorios JPA | Prefijo `I` + sufijo `JpaRepository` | `IThesisJpaRepository` |
| Comandos | Record + sufijo `Command` | `CreateThesisCommand` |
| Handlers | `@Service` + `@Transactional` | `CreateThesisCommandHandler` |
| Controllers | Extienden `BaseV1Controller` | `ThesisController` |
| Formato | google-java-format estilo AOSP | Aplicado via Spotless |
