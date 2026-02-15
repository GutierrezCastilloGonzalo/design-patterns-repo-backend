package com.academicrepo.back.academic_repo.advisors.presentation.controllers;

import com.academicrepo.back.academic_repo.advisors.application.commands.CreateAdvisorCommand;
import com.academicrepo.back.academic_repo.advisors.application.commands.DeactivateAdvisorCommand;
import com.academicrepo.back.academic_repo.advisors.application.commands.UpdateAdvisorCommand;
import com.academicrepo.back.academic_repo.advisors.application.commands.handlers.CreateAdvisorCommandHandler;
import com.academicrepo.back.academic_repo.advisors.application.commands.handlers.DeactivateAdvisorCommandHandler;
import com.academicrepo.back.academic_repo.advisors.application.commands.handlers.UpdateAdvisorCommandHandler;
import com.academicrepo.back.academic_repo.advisors.application.queries.GetAdvisorByIdQuery;
import com.academicrepo.back.academic_repo.advisors.application.queries.handlers.GetAdvisorByIdQueryHandler;
import com.academicrepo.back.academic_repo.advisors.domain.entities.DAdvisor;
import com.academicrepo.back.academic_repo.advisors.domain.repositories.IAdvisorRepository;
import com.academicrepo.back.academic_repo.advisors.presentation.dto.CreateAdvisorDto;
import com.academicrepo.back.academic_repo.advisors.presentation.dto.UpdateAdvisorDto;
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
@RequestMapping("/advisors")
@RequiredArgsConstructor
@Tag(name = "Advisors", description = "Gestion de asesores")
@SecurityRequirement(name = "bearerAuth")
public class AdvisorController extends BaseV1Controller {

    private final CreateAdvisorCommandHandler createHandler;
    private final UpdateAdvisorCommandHandler updateHandler;
    private final DeactivateAdvisorCommandHandler deactivateHandler;
    private final GetAdvisorByIdQueryHandler getByIdHandler;
    private final IAdvisorRepository repository;

    @PostMapping
    @Operation(summary = "Crear asesor")
    public DAdvisor create(@Valid @RequestBody CreateAdvisorDto dto) {
        return createHandler.execute(new CreateAdvisorCommand(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener asesor por ID")
    public DAdvisor getById(@PathVariable Long id) {
        return getByIdHandler.execute(new GetAdvisorByIdQuery(id));
    }

    @GetMapping
    @Operation(summary = "Listar asesores paginados")
    public Page<DAdvisor> getAll(
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
    @Operation(summary = "Actualizar asesor")
    public DAdvisor update(@PathVariable Long id, @Valid @RequestBody UpdateAdvisorDto dto) {
        return updateHandler.execute(new UpdateAdvisorCommand(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar asesor")
    public DAdvisor deactivate(@PathVariable Long id) {
        return deactivateHandler.execute(new DeactivateAdvisorCommand(id));
    }
}
