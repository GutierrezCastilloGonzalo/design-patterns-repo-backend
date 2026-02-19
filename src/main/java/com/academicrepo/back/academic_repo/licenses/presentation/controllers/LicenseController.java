package com.academicrepo.back.academic_repo.licenses.presentation.controllers;

import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;
import com.academicrepo.back.academic_repo.licenses.application.commands.CreateLicenseCommand;
import com.academicrepo.back.academic_repo.licenses.application.commands.DeactivateLicenseCommand;
import com.academicrepo.back.academic_repo.licenses.application.commands.UpdateLicenseCommand;
import com.academicrepo.back.academic_repo.licenses.application.commands.handlers.CreateLicenseCommandHandler;
import com.academicrepo.back.academic_repo.licenses.application.commands.handlers.DeactivateLicenseCommandHandler;
import com.academicrepo.back.academic_repo.licenses.application.commands.handlers.UpdateLicenseCommandHandler;
import com.academicrepo.back.academic_repo.licenses.application.queries.GetLicenseByIdQuery;
import com.academicrepo.back.academic_repo.licenses.application.queries.handlers.GetLicenseByIdQueryHandler;
import com.academicrepo.back.academic_repo.licenses.domain.entities.DLicense;
import com.academicrepo.back.academic_repo.licenses.domain.repositories.ILicenseRepository;
import com.academicrepo.back.academic_repo.licenses.presentation.dto.CreateLicenseDto;
import com.academicrepo.back.academic_repo.licenses.presentation.dto.UpdateLicenseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/licenses")
@RequiredArgsConstructor
@Tag(name = "Licenses", description = "Gestion de licencias")
@SecurityRequirement(name = "bearerAuth")
public class LicenseController extends BaseV1Controller {

    private final CreateLicenseCommandHandler createHandler;
    private final UpdateLicenseCommandHandler updateHandler;
    private final DeactivateLicenseCommandHandler deactivateHandler;
    private final GetLicenseByIdQueryHandler getByIdHandler;
    private final ILicenseRepository repository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Crear licencia con archivo opcional")
    public DLicense create(
            @Valid @RequestPart("data") CreateLicenseDto dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return createHandler.execute(new CreateLicenseCommand(dto, file));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener licencia por ID")
    public DLicense getById(@PathVariable Long id) {
        return getByIdHandler.execute(new GetLicenseByIdQuery(id));
    }

    @GetMapping
    @Operation(summary = "Listar licencias paginadas")
    public Page<DLicense> getAll(
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

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Actualizar licencia con archivo opcional")
    public DLicense update(
            @PathVariable Long id,
            @Valid @RequestPart("data") UpdateLicenseDto dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return updateHandler.execute(new UpdateLicenseCommand(id, dto, file));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar licencia")
    public DLicense deactivate(@PathVariable Long id) {
        return deactivateHandler.execute(new DeactivateLicenseCommand(id));
    }
}
