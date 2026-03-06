package com.academicrepo.back.academic_repo.stats.presentation.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsDto {

    // Totales generales
    private long totalTheses;
    private long totalUsers;
    private long totalAdvisors;
    private long totalAuthors;
    private long totalCommunities;
    private long totalSubcommunities;
    private long totalCollections;
    private long totalDownloadsAllTime;

    // Métricas del mes actual
    private int currentMonth;
    private int currentYear;
    private long thesesPublishedThisMonth;
    private long downloadsOnThisMonthTheses;

    // Tendencias y distribución
    private List<ThesesByYearDto> thesesByYear;
    private List<CollectionThesesCountDto> topCollections;
}
