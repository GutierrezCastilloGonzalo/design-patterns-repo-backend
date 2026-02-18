package com.academicrepo.back.academic_repo.users.presentation.controllers;

import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;
import com.academicrepo.back.academic_repo.general.presentation.exception.ErrorBody;
import com.academicrepo.back.academic_repo.users.application.commands.ActivateUserCommand;
import com.academicrepo.back.academic_repo.users.application.commands.CreateUserCommand;
import com.academicrepo.back.academic_repo.users.application.commands.DeactivateUserCommand;
import com.academicrepo.back.academic_repo.users.application.commands.HardDeleteUserCommand;
import com.academicrepo.back.academic_repo.users.application.commands.UpdateUserCommand;
import com.academicrepo.back.academic_repo.users.application.commands.handlers.ActivateUserCommandHandler;
import com.academicrepo.back.academic_repo.users.application.commands.handlers.CreateUserCommandHandler;
import com.academicrepo.back.academic_repo.users.application.commands.handlers.DeactivateUserCommandHandler;
import com.academicrepo.back.academic_repo.users.application.commands.handlers.HardDeleteUserCommandHandler;
import com.academicrepo.back.academic_repo.users.application.commands.handlers.UpdateUserCommandHandler;
import com.academicrepo.back.academic_repo.users.application.queries.GetUserByIdQuery;
import com.academicrepo.back.academic_repo.users.application.queries.UserPaginatedQuery;
import com.academicrepo.back.academic_repo.users.application.queries.handlers.GetUserByIdQueryHandler;
import com.academicrepo.back.academic_repo.users.application.queries.handlers.UserPaginatedQueryHandler;
import com.academicrepo.back.academic_repo.users.domain.entities.DUser;
import com.academicrepo.back.academic_repo.users.presentation.dto.CreateUserDto;
import com.academicrepo.back.academic_repo.users.presentation.dto.PaginatedUserRequestDto;
import com.academicrepo.back.academic_repo.users.presentation.dto.UpdateUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints para el manejo de usuarios")
public class UserController extends BaseV1Controller {

    private final CreateUserCommandHandler createUserCommandHandler;
    private final UpdateUserCommandHandler updateUserCommandHandler;
    private final DeactivateUserCommandHandler deactivateUserCommandHandler;
    private final ActivateUserCommandHandler activateUserCommandHandler;
    private final HardDeleteUserCommandHandler hardDeleteUserCommandHandler;
    private final GetUserByIdQueryHandler getUserByIdQueryHandler;
    private final UserPaginatedQueryHandler userPaginatedQueryHandler;

    public UserController(
            CreateUserCommandHandler createUserCommandHandler,
            UpdateUserCommandHandler updateUserCommandHandler,
            DeactivateUserCommandHandler deactivateUserCommandHandler,
            ActivateUserCommandHandler activateUserCommandHandler,
            HardDeleteUserCommandHandler hardDeleteUserCommandHandler,
            GetUserByIdQueryHandler getUserByIdQueryHandler,
            UserPaginatedQueryHandler userPaginatedQueryHandler) {
        this.createUserCommandHandler = createUserCommandHandler;
        this.updateUserCommandHandler = updateUserCommandHandler;
        this.deactivateUserCommandHandler = deactivateUserCommandHandler;
        this.activateUserCommandHandler = activateUserCommandHandler;
        this.hardDeleteUserCommandHandler = hardDeleteUserCommandHandler;
        this.getUserByIdQueryHandler = getUserByIdQueryHandler;
        this.userPaginatedQueryHandler = userPaginatedQueryHandler;
    }

    @PostMapping("")
    @Operation(
            summary = "Crear un nuevo usuario",
            description = "Crea un nuevo usuario con la información proporcionada.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Usuario creado exitosamente",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = DUser.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Solicitud inválida",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ErrorBody.class)))
            })
    public DUser createUser(@Valid @RequestBody CreateUserDto dto) {
        return createUserCommandHandler.execute(new CreateUserCommand(dto));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener usuario por ID",
            description = "Obtiene un usuario específico por su ID.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Usuario no encontrado",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ErrorBody.class)))
            })
    public DUser getUserById(@PathVariable Long id) {
        return getUserByIdQueryHandler.execute(new GetUserByIdQuery(id));
    }

    @GetMapping("/paginated")
    @Operation(
            summary = "Obtener usuarios paginados",
            description =
                    "Obtiene una lista paginada de usuarios según la configuración proporcionada.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Lista paginada de usuarios obtenida exitosamente"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Solicitud inválida",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ErrorBody.class)))
            })
    public Page<DUser> getPaginatedUsers(@ModelAttribute PaginatedUserRequestDto requestDto) {
        requestDto.normalizePageNumber();
        UserPaginatedQuery query = new UserPaginatedQuery(requestDto);
        return userPaginatedQueryHandler.execute(query);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza la información de un usuario existente.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Usuario actualizado exitosamente",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = DUser.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Solicitud inválida",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ErrorBody.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Usuario no encontrado",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ErrorBody.class)))
            })
    public DUser updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDto dto) {
        return updateUserCommandHandler.execute(new UpdateUserCommand(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Desactivar usuario",
            description = "Desactiva (soft delete) un usuario existente.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Usuario desactivado exitosamente",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = DUser.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Usuario no encontrado",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ErrorBody.class)))
            })
    public DUser deactivateUser(@PathVariable Long id) {
        return deactivateUserCommandHandler.execute(new DeactivateUserCommand(id));
    }

    @PutMapping("/{id}/activate")
    @Operation(
            summary = "Activar usuario",
            description = "Reactiva un usuario previamente desactivado.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Usuario activado exitosamente",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = DUser.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Usuario no encontrado",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ErrorBody.class)))
            })
    public DUser activateUser(@PathVariable Long id) {
        return activateUserCommandHandler.execute(new ActivateUserCommand(id));
    }

    @DeleteMapping("/{id}/permanent")
    @Operation(
            summary = "Eliminar usuario permanentemente",
            description =
                    "Elimina físicamente un usuario de la base de datos. Esta acción no se puede deshacer.",
            responses = {
                @ApiResponse(
                        responseCode = "204",
                        description = "Usuario eliminado permanentemente"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Usuario no encontrado",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ErrorBody.class)))
            })
    public void hardDeleteUser(@PathVariable Long id) {
        hardDeleteUserCommandHandler.execute(new HardDeleteUserCommand(id));
    }
}
