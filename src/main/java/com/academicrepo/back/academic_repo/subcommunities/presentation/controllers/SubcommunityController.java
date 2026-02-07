package com.academicrepo.back.academic_repo.subcommunities.presentation.controllers;

import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;
import com.academicrepo.back.academic_repo.subcommunities.application.commands.CreateSubcommunityCommand;
import com.academicrepo.back.academic_repo.subcommunities.application.commands.DeactivateSubcommunityCommand;
import com.academicrepo.back.academic_repo.subcommunities.application.commands.UpdateSubcommunityCommand;
import com.academicrepo.back.academic_repo.subcommunities.application.commands.handlers.CreateSubcommunityCommandHandler;
import com.academicrepo.back.academic_repo.subcommunities.application.commands.handlers.DeactivateSubcommunityCommandHandler;
import com.academicrepo.back.academic_repo.subcommunities.application.commands.handlers.UpdateSubcommunityCommandHandler;
import com.academicrepo.back.academic_repo.subcommunities.application.queries.GetSubcommunityByIdQuery;
import com.academicrepo.back.academic_repo.subcommunities.application.queries.handlers.GetSubcommunityByIdQueryHandler;
import com.academicrepo.back.academic_repo.subcommunities.domain.entities.DSubcommunity;
import com.academicrepo.back.academic_repo.subcommunities.domain.repositories.ISubcommunityRepository;
import com.academicrepo.back.academic_repo.subcommunities.presentation.dto.CreateSubcommunityDto;
import com.academicrepo.back.academic_repo.subcommunities.presentation.dto.UpdateSubcommunityDto;
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
@RequestMapping("/v1/subcommunities")
@RequiredArgsConstructor
@Tag(name = "Subcommunities", description = "Gestión de subcomunidades")
@SecurityRequirement(name = "bearerAuth")
public class SubcommunityController extends BaseV1Controller {

    private final CreateSubcommunityCommandHandler createHandler;
    private final UpdateSubcommunityCommandHandler updateHandler;
    private final DeactivateSubcommunityCommandHandler deactivateHandler;
    private final GetSubcommunityByIdQueryHandler getByIdHandler;
    private final ISubcommunityRepository repository;

    @PostMapping
    @Operation(summary = "Crear subcomunidad")
    public DSubcommunity create(@Valid @RequestBody CreateSubcommunityDto dto) {
        return createHandler.execute(new CreateSubcommunityCommand(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener subcomunidad por ID")
    public DSubcommunity getById(@PathVariable Long id) {
        return getByIdHandler.execute(new GetSubcommunityByIdQuery(id));
    }

    @GetMapping
    @Operation(summary = "Listar subcomunidades paginadas")
    public Page<DSubcommunity> getAll(
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

    @GetMapping("/community/{communityId}")
    @Operation(summary = "Listar subcomunidades por comunidad")
    public Page<DSubcommunity> getByCommunity(
            @PathVariable Long communityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return repository.findByCommunityId(communityId, PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar subcomunidad")
    public DSubcommunity update(
            @PathVariable Long id, @Valid @RequestBody UpdateSubcommunityDto dto) {
        return updateHandler.execute(new UpdateSubcommunityCommand(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar subcomunidad")
    public DSubcommunity deactivate(@PathVariable Long id) {
        return deactivateHandler.execute(new DeactivateSubcommunityCommand(id));
    }
}
