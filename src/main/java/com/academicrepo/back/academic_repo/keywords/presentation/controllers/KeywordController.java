package com.academicrepo.back.academic_repo.keywords.presentation.controllers;

import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;
import com.academicrepo.back.academic_repo.keywords.application.commands.CreateKeywordCommand;
import com.academicrepo.back.academic_repo.keywords.application.commands.DeactivateKeywordCommand;
import com.academicrepo.back.academic_repo.keywords.application.commands.UpdateKeywordCommand;
import com.academicrepo.back.academic_repo.keywords.application.commands.handlers.CreateKeywordCommandHandler;
import com.academicrepo.back.academic_repo.keywords.application.commands.handlers.DeactivateKeywordCommandHandler;
import com.academicrepo.back.academic_repo.keywords.application.commands.handlers.UpdateKeywordCommandHandler;
import com.academicrepo.back.academic_repo.keywords.application.queries.GetKeywordByIdQuery;
import com.academicrepo.back.academic_repo.keywords.application.queries.handlers.GetKeywordByIdQueryHandler;
import com.academicrepo.back.academic_repo.keywords.domain.entities.DKeyword;
import com.academicrepo.back.academic_repo.keywords.domain.repositories.IKeywordRepository;
import com.academicrepo.back.academic_repo.keywords.presentation.dto.CreateKeywordDto;
import com.academicrepo.back.academic_repo.keywords.presentation.dto.UpdateKeywordDto;
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
@RequestMapping("/keywords")
@RequiredArgsConstructor
@Tag(name = "Keywords", description = "Gestion de palabras clave")
@SecurityRequirement(name = "bearerAuth")
public class KeywordController extends BaseV1Controller {

    private final CreateKeywordCommandHandler createHandler;
    private final UpdateKeywordCommandHandler updateHandler;
    private final DeactivateKeywordCommandHandler deactivateHandler;
    private final GetKeywordByIdQueryHandler getByIdHandler;
    private final IKeywordRepository repository;

    @PostMapping
    @Operation(summary = "Crear palabra clave")
    public DKeyword create(@Valid @RequestBody CreateKeywordDto dto) {
        return createHandler.execute(new CreateKeywordCommand(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener palabra clave por ID")
    public DKeyword getById(@PathVariable Long id) {
        return getByIdHandler.execute(new GetKeywordByIdQuery(id));
    }

    @GetMapping
    @Operation(summary = "Listar palabras clave paginadas")
    public Page<DKeyword> getAll(
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

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar palabra clave")
    public DKeyword update(@PathVariable Long id, @Valid @RequestBody UpdateKeywordDto dto) {
        return updateHandler.execute(new UpdateKeywordCommand(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar palabra clave")
    public DKeyword deactivate(@PathVariable Long id) {
        return deactivateHandler.execute(new DeactivateKeywordCommand(id));
    }
}
