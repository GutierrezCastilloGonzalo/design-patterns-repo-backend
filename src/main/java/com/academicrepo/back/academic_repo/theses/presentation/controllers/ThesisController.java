package com.academicrepo.back.academic_repo.theses.presentation.controllers;

import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;
import com.academicrepo.back.academic_repo.theses.application.commands.CreateThesisCommand;
import com.academicrepo.back.academic_repo.theses.application.commands.DeactivateThesisCommand;
import com.academicrepo.back.academic_repo.theses.application.commands.UpdateThesisCommand;
import com.academicrepo.back.academic_repo.theses.application.commands.handlers.CreateThesisCommandHandler;
import com.academicrepo.back.academic_repo.theses.application.commands.handlers.DeactivateThesisCommandHandler;
import com.academicrepo.back.academic_repo.theses.application.commands.handlers.UpdateThesisCommandHandler;
import com.academicrepo.back.academic_repo.theses.application.queries.GetThesisByIdQuery;
import com.academicrepo.back.academic_repo.theses.application.queries.handlers.GetThesisByIdQueryHandler;
import com.academicrepo.back.academic_repo.theses.domain.entities.DThesis;
import com.academicrepo.back.academic_repo.theses.domain.repositories.IThesisRepository;
import com.academicrepo.back.academic_repo.theses.presentation.dto.CreateThesisDto;
import com.academicrepo.back.academic_repo.theses.presentation.dto.UpdateThesisDto;
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
@RequestMapping("/theses")
@RequiredArgsConstructor
@Tag(name = "Theses", description = "Gestión de tesis")
@SecurityRequirement(name = "bearerAuth")
public class ThesisController extends BaseV1Controller {

    private final CreateThesisCommandHandler createHandler;
    private final UpdateThesisCommandHandler updateHandler;
    private final DeactivateThesisCommandHandler deactivateHandler;
    private final GetThesisByIdQueryHandler getByIdHandler;
    private final IThesisRepository repository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Crear tesis con archivo opcional")
    public DThesis create(
            @Valid @RequestPart("data") CreateThesisDto dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return createHandler.execute(new CreateThesisCommand(dto, file));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tesis por ID")
    public DThesis getById(@PathVariable Long id) {
        return getByIdHandler.execute(new GetThesisByIdQuery(id));
    }

    @GetMapping
    @Operation(summary = "Listar tesis paginadas")
    public Page<DThesis> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort =
                sortDir.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();
        return repository.findAll(PageRequest.of(Math.max(0, page - 1), size, sort));
    }

    @GetMapping("/collection/{collectionId}")
    @Operation(summary = "Listar tesis por colección")
    public Page<DThesis> getByCollection(
            @PathVariable Long collectionId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return repository.findByCollectionId(
                collectionId, PageRequest.of(Math.max(0, page - 1), size));
    }

    @GetMapping("/advisor/{advisorId}")
    @Operation(summary = "Listar tesis por asesor")
    public Page<DThesis> getByAdvisor(
            @PathVariable Long advisorId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return repository.findByAdvisorId(advisorId, PageRequest.of(Math.max(0, page - 1), size));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Actualizar tesis con archivo opcional")
    public DThesis update(
            @PathVariable Long id,
            @Valid @RequestPart("data") UpdateThesisDto dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return updateHandler.execute(new UpdateThesisCommand(id, dto, file));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar tesis")
    public DThesis deactivate(@PathVariable Long id) {
        return deactivateHandler.execute(new DeactivateThesisCommand(id));
    }
}
