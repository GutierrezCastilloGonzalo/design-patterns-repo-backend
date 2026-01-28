package com.academicrepo.back.academic_repo.communities.presentation.controllers;

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

import com.academicrepo.back.academic_repo.communities.application.commands.CreateCommunityCommand;
import com.academicrepo.back.academic_repo.communities.application.commands.DeactivateCommunityCommand;
import com.academicrepo.back.academic_repo.communities.application.commands.UpdateCommunityCommand;
import com.academicrepo.back.academic_repo.communities.application.commands.handlers.CreateCommunityCommandHandler;
import com.academicrepo.back.academic_repo.communities.application.commands.handlers.DeactivateCommunityCommandHandler;
import com.academicrepo.back.academic_repo.communities.application.commands.handlers.UpdateCommunityCommandHandler;
import com.academicrepo.back.academic_repo.communities.application.queries.GetCommunityByIdQuery;
import com.academicrepo.back.academic_repo.communities.application.queries.handlers.GetCommunityByIdQueryHandler;
import com.academicrepo.back.academic_repo.communities.domain.entities.DCommunity;
import com.academicrepo.back.academic_repo.communities.domain.repositories.ICommunityRepository;
import com.academicrepo.back.academic_repo.communities.presentation.dto.CreateCommunityDto;
import com.academicrepo.back.academic_repo.communities.presentation.dto.UpdateCommunityDto;
import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/communities")
@RequiredArgsConstructor
@Tag(name = "Communities", description = "Gestión de comunidades")
@SecurityRequirement(name = "bearerAuth")
public class CommunityController extends BaseV1Controller {

    private final CreateCommunityCommandHandler createHandler;
    private final UpdateCommunityCommandHandler updateHandler;
    private final DeactivateCommunityCommandHandler deactivateHandler;
    private final GetCommunityByIdQueryHandler getByIdHandler;
    private final ICommunityRepository repository;

    @PostMapping
    @Operation(summary = "Crear comunidad")
    public DCommunity create(@Valid @RequestBody CreateCommunityDto dto) {
        return createHandler.execute(new CreateCommunityCommand(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener comunidad por ID")
    public DCommunity getById(@PathVariable Long id) {
        return getByIdHandler.execute(new GetCommunityByIdQuery(id));
    }

    @GetMapping
    @Operation(summary = "Listar comunidades paginadas")
    public Page<DCommunity> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return repository.findAll(PageRequest.of(page, size, sort));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar comunidad")
    public DCommunity update(@PathVariable Long id, @Valid @RequestBody UpdateCommunityDto dto) {
        return updateHandler.execute(new UpdateCommunityCommand(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar comunidad")
    public DCommunity deactivate(@PathVariable Long id) {
        return deactivateHandler.execute(new DeactivateCommunityCommand(id));
    }
}
