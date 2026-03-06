package com.academicrepo.back.academic_repo.stats.presentation.controllers;

import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;
import com.academicrepo.back.academic_repo.stats.application.StatsQueryService;
import com.academicrepo.back.academic_repo.stats.presentation.dto.AdvisorThesesDto;
import com.academicrepo.back.academic_repo.stats.presentation.dto.AuthorPublicationsDto;
import com.academicrepo.back.academic_repo.stats.presentation.dto.DashboardStatsDto;
import com.academicrepo.back.academic_repo.stats.presentation.dto.ThesesByYearDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/stats")
@RequiredArgsConstructor
@Tag(name = "Stats", description = "Estadísticas públicas para el dashboard")
public class StatsController extends BaseV1Controller {

    private final StatsQueryService statsService;

    @GetMapping
    @Operation(
            summary = "Dashboard principal",
            description =
                    "Retorna conteos generales, métricas del mes actual,"
                            + " tesis por año y top colecciones")
    public DashboardStatsDto getDashboard() {
        return statsService.getDashboardStats();
    }

    @GetMapping("/top-authors")
    @Operation(
            summary = "Autores con más publicaciones",
            description = "Lista de autores ordenados por cantidad de tesis publicadas")
    public List<AuthorPublicationsDto> getTopAuthors(
            @RequestParam(defaultValue = "10") int limit) {
        return statsService.getTopAuthors(Math.min(limit, 50));
    }

    @GetMapping("/active-advisors")
    @Operation(
            summary = "Asesores vinculados a tesis",
            description = "Lista de asesores con al menos una tesis activa, ordenados por cantidad")
    public List<AdvisorThesesDto> getActiveAdvisors() {
        return statsService.getActiveAdvisors();
    }

    @GetMapping("/theses-by-year")
    @Operation(
            summary = "Tesis por año",
            description = "Cantidad de tesis publicadas por año (útil para gráficos de tendencia)")
    public List<ThesesByYearDto> getThesesByYear() {
        return statsService.getThesesByYear();
    }
}
