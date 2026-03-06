package com.academicrepo.back.academic_repo.stats.application;

import com.academicrepo.back.academic_repo.advisors.infrastructure.entities.Advisor;
import com.academicrepo.back.academic_repo.advisors.infrastructure.repositories.interfaces.IAdvisorJpaRepository;
import com.academicrepo.back.academic_repo.authors.infrastructure.entities.Author;
import com.academicrepo.back.academic_repo.authors.infrastructure.repositories.interfaces.IAuthorJpaRepository;
import com.academicrepo.back.academic_repo.collections.infrastructure.entities.Collection;
import com.academicrepo.back.academic_repo.collections.infrastructure.repositories.interfaces.ICollectionJpaRepository;
import com.academicrepo.back.academic_repo.communities.infrastructure.repositories.interfaces.ICommunityJpaRepository;
import com.academicrepo.back.academic_repo.stats.presentation.dto.AdvisorThesesDto;
import com.academicrepo.back.academic_repo.stats.presentation.dto.AuthorPublicationsDto;
import com.academicrepo.back.academic_repo.stats.presentation.dto.CollectionThesesCountDto;
import com.academicrepo.back.academic_repo.stats.presentation.dto.DashboardStatsDto;
import com.academicrepo.back.academic_repo.stats.presentation.dto.ThesesByYearDto;
import com.academicrepo.back.academic_repo.subcommunities.infrastructure.repositories.interfaces.ISubcommunityJpaRepository;
import com.academicrepo.back.academic_repo.theses.infrastructure.repositories.interfaces.IThesisJpaRepository;
import com.academicrepo.back.academic_repo.users.infrastructure.repositories.interfaces.IUserJpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsQueryService {

    private final IThesisJpaRepository thesisRepo;
    private final IUserJpaRepository userRepo;
    private final IAdvisorJpaRepository advisorRepo;
    private final IAuthorJpaRepository authorRepo;
    private final ICommunityJpaRepository communityRepo;
    private final ISubcommunityJpaRepository subcommunityRepo;
    private final ICollectionJpaRepository collectionRepo;

    public DashboardStatsDto getDashboardStats() {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        List<ThesesByYearDto> thesesByYear =
                thesisRepo.countByPublicationYear().stream()
                        .map(
                                row ->
                                        new ThesesByYearDto(
                                                ((Number) row[0]).intValue(),
                                                ((Number) row[1]).longValue()))
                        .collect(Collectors.toList());

        List<Object[]> topCollectionsRaw =
                thesisRepo.countByCollectionIdGrouped(PageRequest.of(0, 10));

        List<Long> collectionIds =
                topCollectionsRaw.stream()
                        .map(row -> ((Number) row[0]).longValue())
                        .collect(Collectors.toList());

        Map<Long, String> collectionNameMap =
                collectionRepo.findByIdIn(collectionIds).stream()
                        .collect(Collectors.toMap(Collection::getId, Collection::getName));

        List<CollectionThesesCountDto> topCollections =
                topCollectionsRaw.stream()
                        .map(
                                row -> {
                                    Long colId = ((Number) row[0]).longValue();
                                    Long count = ((Number) row[1]).longValue();
                                    String name =
                                            collectionNameMap.getOrDefault(
                                                    colId, "Colección #" + colId);
                                    return new CollectionThesesCountDto(colId, name, count);
                                })
                        .collect(Collectors.toList());

        return DashboardStatsDto.builder()
                .totalTheses(thesisRepo.countByIsActiveTrue())
                .totalUsers(userRepo.countByIsActiveTrue())
                .totalAdvisors(advisorRepo.countByIsActiveTrue())
                .totalAuthors(authorRepo.countByIsActiveTrue())
                .totalCommunities(communityRepo.countByIsActiveTrue())
                .totalSubcommunities(subcommunityRepo.countByIsActiveTrue())
                .totalCollections(collectionRepo.countByIsActiveTrue())
                .totalDownloadsAllTime(
                        nullToZero(thesisRepo.sumTotalDownloads()))
                .currentMonth(month)
                .currentYear(year)
                .thesesPublishedThisMonth(thesisRepo.countPublishedInMonth(month, year))
                .downloadsOnThisMonthTheses(
                        nullToZero(thesisRepo.sumDownloadsForMonth(month, year)))
                .thesesByYear(thesesByYear)
                .topCollections(topCollections)
                .build();
    }

    public List<AuthorPublicationsDto> getTopAuthors(int limit) {
        List<Object[]> rows =
                thesisRepo.findTopAuthorIdsWithCount(PageRequest.of(0, limit));

        List<Long> authorIds =
                rows.stream()
                        .map(row -> ((Number) row[0]).longValue())
                        .collect(Collectors.toList());

        Map<Long, Author> authorMap =
                authorRepo.findByIdInAndIsActiveTrue(authorIds).stream()
                        .collect(Collectors.toMap(Author::getId, a -> a));

        return rows.stream()
                .map(
                        row -> {
                            Long authorId = ((Number) row[0]).longValue();
                            Long count = ((Number) row[1]).longValue();
                            Author author = authorMap.get(authorId);
                            String fullName =
                                    author != null
                                            ? (author.getFirstName() + " " + author.getLastName())
                                                    .trim()
                                            : "Autor #" + authorId;
                            String affiliation = author != null ? author.getAffiliation() : null;
                            return new AuthorPublicationsDto(authorId, fullName, affiliation, count);
                        })
                .collect(Collectors.toList());
    }

    public List<AdvisorThesesDto> getActiveAdvisors() {
        List<Object[]> rows = thesisRepo.findAdvisorIdsWithThesisCount();

        List<Long> advisorIds =
                rows.stream()
                        .map(row -> ((Number) row[0]).longValue())
                        .collect(Collectors.toList());

        Map<Long, Advisor> advisorMap =
                advisorRepo.findByIdInAndIsActiveTrue(advisorIds).stream()
                        .collect(Collectors.toMap(Advisor::getId, a -> a));

        return rows.stream()
                .map(
                        row -> {
                            Long advisorId = ((Number) row[0]).longValue();
                            Long count = ((Number) row[1]).longValue();
                            Advisor advisor = advisorMap.get(advisorId);
                            String fullName =
                                    advisor != null
                                            ? (advisor.getFirstName() + " " + advisor.getLastName())
                                                    .trim()
                                            : "Asesor #" + advisorId;
                            String department = advisor != null ? advisor.getDepartment() : null;
                            String title = advisor != null ? advisor.getTitle() : null;
                            return new AdvisorThesesDto(advisorId, fullName, department, title, count);
                        })
                .collect(Collectors.toList());
    }

    public List<ThesesByYearDto> getThesesByYear() {
        return thesisRepo.countByPublicationYear().stream()
                .map(
                        row ->
                                new ThesesByYearDto(
                                        ((Number) row[0]).intValue(),
                                        ((Number) row[1]).longValue()))
                .collect(Collectors.toList());
    }

    private long nullToZero(Long value) {
        return value != null ? value : 0L;
    }
}
