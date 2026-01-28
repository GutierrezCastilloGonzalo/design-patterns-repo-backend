package com.academicrepo.back.academic_repo.collections.presentation.controllers;

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

import com.academicrepo.back.academic_repo.collections.application.commands.CreateCollectionCommand;
import com.academicrepo.back.academic_repo.collections.application.commands.DeactivateCollectionCommand;
import com.academicrepo.back.academic_repo.collections.application.commands.UpdateCollectionCommand;
import com.academicrepo.back.academic_repo.collections.application.commands.handlers.CreateCollectionCommandHandler;
import com.academicrepo.back.academic_repo.collections.application.commands.handlers.DeactivateCollectionCommandHandler;
import com.academicrepo.back.academic_repo.collections.application.commands.handlers.UpdateCollectionCommandHandler;
import com.academicrepo.back.academic_repo.collections.application.queries.GetCollectionByIdQuery;
import com.academicrepo.back.academic_repo.collections.application.queries.handlers.GetCollectionByIdQueryHandler;
import com.academicrepo.back.academic_repo.collections.domain.entities.DCollection;
import com.academicrepo.back.academic_repo.collections.domain.repositories.ICollectionRepository;
import com.academicrepo.back.academic_repo.collections.presentation.dto.CreateCollectionDto;
import com.academicrepo.back.academic_repo.collections.presentation.dto.UpdateCollectionDto;
import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
@Tag(name = "Collections", description = "Gestion de colecciones")
@SecurityRequirement(name = "bearerAuth")
public class CollectionController extends BaseV1Controller {

    private final CreateCollectionCommandHandler createHandler;
    private final UpdateCollectionCommandHandler updateHandler;
    private final DeactivateCollectionCommandHandler deactivateHandler;
    private final GetCollectionByIdQueryHandler getByIdHandler;
    private final ICollectionRepository repository;

    @PostMapping
    @Operation(summary = "Crear coleccion")
    public DCollection create(@Valid @RequestBody CreateCollectionDto dto) {
        return createHandler.execute(new CreateCollectionCommand(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener coleccion por ID")
    public DCollection getById(@PathVariable Long id) {
        return getByIdHandler.execute(new GetCollectionByIdQuery(id));
    }

    @GetMapping
    @Operation(summary = "Listar colecciones paginadas")
    public Page<DCollection> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return repository.findAll(PageRequest.of(page, size, sort));
    }

    @GetMapping("/subcommunity/{subcommunityId}")
    @Operation(summary = "Listar colecciones por subcomunidad")
    public Page<DCollection> getBySubcommunity(
            @PathVariable Long subcommunityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return repository.findBySubcommunityId(subcommunityId, PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar coleccion")
    public DCollection update(@PathVariable Long id, @Valid @RequestBody UpdateCollectionDto dto) {
        return updateHandler.execute(new UpdateCollectionCommand(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar coleccion")
    public DCollection deactivate(@PathVariable Long id) {
        return deactivateHandler.execute(new DeactivateCollectionCommand(id));
    }
}
