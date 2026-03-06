package com.academicrepo.back.academic_repo.theses.presentation.controllers;

import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;
import com.academicrepo.back.academic_repo.theses.application.commands.IncrementDownloadCountCommand;
import com.academicrepo.back.academic_repo.theses.application.commands.IncrementViewCountCommand;
import com.academicrepo.back.academic_repo.theses.application.commands.handlers.IncrementDownloadCountCommandHandler;
import com.academicrepo.back.academic_repo.theses.application.commands.handlers.IncrementViewCountCommandHandler;
import com.academicrepo.back.academic_repo.theses.application.queries.GetThesesPublicQuery;
import com.academicrepo.back.academic_repo.theses.application.queries.GetThesisPublicDetailQuery;
import com.academicrepo.back.academic_repo.theses.application.queries.GetTopDownloadedThesesQuery;
import com.academicrepo.back.academic_repo.theses.application.queries.SearchThesesQuery;
import com.academicrepo.back.academic_repo.theses.application.queries.handlers.GetThesesPublicQueryHandler;
import com.academicrepo.back.academic_repo.theses.application.queries.handlers.GetThesisPublicDetailQueryHandler;
import com.academicrepo.back.academic_repo.theses.application.queries.handlers.GetTopDownloadedThesesQueryHandler;
import com.academicrepo.back.academic_repo.theses.application.queries.handlers.SearchThesesQueryHandler;
import com.academicrepo.back.academic_repo.theses.presentation.dto.ThesisPublicDto;
import com.academicrepo.back.academic_repo.theses.presentation.dto.ThesisTopDownloadedDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/theses")
@RequiredArgsConstructor
@Tag(name = "Public", description = "Endpoints públicos sin autenticación")
public class PublicController extends BaseV1Controller {

    private final GetThesesPublicQueryHandler handler;
    private final GetThesisPublicDetailQueryHandler detailHandler;
    private final GetTopDownloadedThesesQueryHandler topDownloadedHandler;
    private final IncrementDownloadCountCommandHandler incrementDownloadHandler;
    private final IncrementViewCountCommandHandler incrementViewHandler;
    private final SearchThesesQueryHandler searchHandler;

    @GetMapping
    @Operation(summary = "Listar tesis con keywords (sin autenticación)")
    public Page<ThesisPublicDto> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return handler.execute(new GetThesesPublicQuery(page, size, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de una tesis (sin autenticación). Incrementa el contador de vistas.")
    public ThesisPublicDto getById(@PathVariable Long id) {
        ThesisPublicDto dto = detailHandler.execute(new GetThesisPublicDetailQuery(id));
        incrementViewHandler.execute(new IncrementViewCountCommand(id));
        return dto;
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar tesis por año, título, autor o colección (sin autenticación)")
    public Page<ThesisPublicDto> search(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long collectionId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return searchHandler.execute(
                new SearchThesesQuery(year, title, authorId, collectionId, page, size, sortBy, sortDir));
    }

    @GetMapping("/top-downloaded")
    @Operation(summary = "Top 5 tesis más descargadas (sin autenticación)")
    public List<ThesisTopDownloadedDto> getTopDownloaded() {
        return topDownloadedHandler.execute(new GetTopDownloadedThesesQuery(5));
    }

    @GetMapping("/{id}/download")
    @Operation(summary = "Descargar tesis (sin autenticación). Incrementa el contador de descargas.")
    public ResponseEntity<Void> download(@PathVariable Long id) {
        ThesisPublicDto dto = detailHandler.execute(new GetThesisPublicDetailQuery(id));
        incrementDownloadHandler.execute(new IncrementDownloadCountCommand(id));
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, dto.getFileUrl())
                .build();
    }
}
