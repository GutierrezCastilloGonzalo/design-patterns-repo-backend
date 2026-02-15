package com.academicrepo.back.academic_repo.authors.presentation.controllers;

import com.academicrepo.back.academic_repo.authors.application.commands.CreateAuthorCommand;
import com.academicrepo.back.academic_repo.authors.application.commands.DeactivateAuthorCommand;
import com.academicrepo.back.academic_repo.authors.application.commands.UpdateAuthorCommand;
import com.academicrepo.back.academic_repo.authors.application.commands.handlers.CreateAuthorCommandHandler;
import com.academicrepo.back.academic_repo.authors.application.commands.handlers.DeactivateAuthorCommandHandler;
import com.academicrepo.back.academic_repo.authors.application.commands.handlers.UpdateAuthorCommandHandler;
import com.academicrepo.back.academic_repo.authors.application.queries.GetAuthorByIdQuery;
import com.academicrepo.back.academic_repo.authors.application.queries.handlers.GetAuthorByIdQueryHandler;
import com.academicrepo.back.academic_repo.authors.domain.entities.DAuthor;
import com.academicrepo.back.academic_repo.authors.domain.repositories.IAuthorRepository;
import com.academicrepo.back.academic_repo.authors.presentation.dto.CreateAuthorDto;
import com.academicrepo.back.academic_repo.authors.presentation.dto.UpdateAuthorDto;
import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "Gestion de autores")
@SecurityRequirement(name = "bearerAuth")
public class AuthorController extends BaseV1Controller {

    private final CreateAuthorCommandHandler createHandler;
    private final UpdateAuthorCommandHandler updateHandler;
    private final DeactivateAuthorCommandHandler deactivateHandler;
    private final GetAuthorByIdQueryHandler getByIdHandler;
    private final IAuthorRepository repository;

    @PostMapping
    @Operation(summary = "Crear autor")
    public DAuthor create(@Valid @RequestBody CreateAuthorDto dto) {
        return createHandler.execute(new CreateAuthorCommand(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener autor por ID")
    public DAuthor getById(@PathVariable Long id) {
        return getByIdHandler.execute(new GetAuthorByIdQuery(id));
    }

    @GetMapping
    @Operation(summary = "Listar autores paginados")
    public Page<DAuthor> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort =
                sortDir.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();
        return repository.findAll(PageRequest.of(page, size, sort));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar autor")
    public DAuthor update(@PathVariable Long id, @Valid @RequestBody UpdateAuthorDto dto) {
        return updateHandler.execute(new UpdateAuthorCommand(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar autor")
    public DAuthor deactivate(@PathVariable Long id) {
        return deactivateHandler.execute(new DeactivateAuthorCommand(id));
    }
}
