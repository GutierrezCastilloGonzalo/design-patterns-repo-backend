package com.academicrepo.back.academic_repo.theses.presentation.controllers;

import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;
import com.academicrepo.back.academic_repo.theses.application.queries.GetThesesPublicQuery;
import com.academicrepo.back.academic_repo.theses.application.queries.handlers.GetThesesPublicQueryHandler;
import com.academicrepo.back.academic_repo.theses.presentation.dto.ThesisPublicDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/theses")
@RequiredArgsConstructor
@Tag(name = "Public", description = "Endpoints públicos sin autenticación")
public class PublicController extends BaseV1Controller {

    private final GetThesesPublicQueryHandler handler;

    @GetMapping
    @Operation(summary = "Listar tesis con keywords (sin autenticación)")
    public Page<ThesisPublicDto> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return handler.execute(new GetThesesPublicQuery(page, size, sortBy, sortDir));
    }
}
