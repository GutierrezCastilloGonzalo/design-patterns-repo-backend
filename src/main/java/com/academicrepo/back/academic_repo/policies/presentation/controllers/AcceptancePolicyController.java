package com.academicrepo.back.academic_repo.policies.presentation.controllers;

import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;
import com.academicrepo.back.academic_repo.policies.application.commands.CreateAcceptancePolicyCommand;
import com.academicrepo.back.academic_repo.policies.application.commands.DeactivateAcceptancePolicyCommand;
import com.academicrepo.back.academic_repo.policies.application.commands.UpdateAcceptancePolicyCommand;
import com.academicrepo.back.academic_repo.policies.application.commands.handlers.CreateAcceptancePolicyCommandHandler;
import com.academicrepo.back.academic_repo.policies.application.commands.handlers.DeactivateAcceptancePolicyCommandHandler;
import com.academicrepo.back.academic_repo.policies.application.commands.handlers.UpdateAcceptancePolicyCommandHandler;
import com.academicrepo.back.academic_repo.policies.application.queries.GetAcceptancePolicyByIdQuery;
import com.academicrepo.back.academic_repo.policies.application.queries.handlers.GetAcceptancePolicyByIdQueryHandler;
import com.academicrepo.back.academic_repo.policies.domain.entities.DAcceptancePolicy;
import com.academicrepo.back.academic_repo.policies.domain.repositories.IAcceptancePolicyRepository;
import com.academicrepo.back.academic_repo.policies.presentation.dto.CreateAcceptancePolicyDto;
import com.academicrepo.back.academic_repo.policies.presentation.dto.UpdateAcceptancePolicyDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/acceptance-policies")
@RequiredArgsConstructor
@Tag(name = "Acceptance Policies", description = "Gestion de politicas de aceptacion")
@SecurityRequirement(name = "bearerAuth")
public class AcceptancePolicyController extends BaseV1Controller {

    private final CreateAcceptancePolicyCommandHandler createHandler;
    private final UpdateAcceptancePolicyCommandHandler updateHandler;
    private final DeactivateAcceptancePolicyCommandHandler deactivateHandler;
    private final GetAcceptancePolicyByIdQueryHandler getByIdHandler;
    private final IAcceptancePolicyRepository repository;

    @PostMapping
    @Operation(summary = "Crear politica de aceptacion")
    public DAcceptancePolicy create(@Valid @RequestBody CreateAcceptancePolicyDto dto) {
        return createHandler.execute(new CreateAcceptancePolicyCommand(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener politica por ID")
    public DAcceptancePolicy getById(@PathVariable Long id) {
        return getByIdHandler.execute(new GetAcceptancePolicyByIdQuery(id));
    }

    @GetMapping
    @Operation(summary = "Listar politicas paginadas")
    public Page<DAcceptancePolicy> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort =
                sortDir.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();
        return repository.findAll(PageRequest.of(Math.max(0, page - 1), size, sort));
    }

    @GetMapping("/all")
    @Operation(summary = "Listar todas las politicas activas")
    public List<DAcceptancePolicy> getAllActive() {
        return repository.findAllActive();
    }

    @GetMapping("/required")
    @Operation(summary = "Listar politicas requeridas")
    public List<DAcceptancePolicy> getRequired() {
        return repository.findRequired();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar politica")
    public DAcceptancePolicy update(
            @PathVariable Long id, @Valid @RequestBody UpdateAcceptancePolicyDto dto) {
        return updateHandler.execute(new UpdateAcceptancePolicyCommand(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar politica")
    public DAcceptancePolicy deactivate(@PathVariable Long id) {
        return deactivateHandler.execute(new DeactivateAcceptancePolicyCommand(id));
    }
}
