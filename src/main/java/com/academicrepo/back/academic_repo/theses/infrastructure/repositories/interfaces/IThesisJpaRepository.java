package com.academicrepo.back.academic_repo.theses.infrastructure.repositories.interfaces;

import com.academicrepo.back.academic_repo.theses.infrastructure.entities.Thesis;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IThesisJpaRepository extends JpaRepository<Thesis, Long> {
    Optional<Thesis> findByIdAndIsActiveTrue(Long id);

    Page<Thesis> findByIsActiveTrue(Pageable pageable);

    Page<Thesis> findByCollectionIdAndIsActiveTrue(Long collectionId, Pageable pageable);

    Page<Thesis> findByAdvisorIdAndIsActiveTrue(Long advisorId, Pageable pageable);

    boolean existsByTitleAndCollectionId(String title, Long collectionId);

    @Query("SELECT t FROM Thesis t WHERE t.isActive = true ORDER BY t.nDescargas DESC")
    List<Thesis> findTopByDownloads(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Thesis t SET t.nDescargas = t.nDescargas + 1 WHERE t.id = :id")
    void incrementDownloadCount(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Thesis t SET t.nVistas = t.nVistas + 1 WHERE t.id = :id")
    void incrementViewCount(@Param("id") Long id);

    long countByIsActiveTrue();

    @Query("SELECT COALESCE(SUM(t.nDescargas), 0) FROM Thesis t WHERE t.isActive = true")
    Long sumTotalDownloads();

    @Query(
            "SELECT COUNT(t) FROM Thesis t WHERE t.isActive = true"
                    + " AND month(t.publicationDate) = :month"
                    + " AND year(t.publicationDate) = :year")
    long countPublishedInMonth(@Param("month") int month, @Param("year") int year);

    @Query(
            "SELECT COALESCE(SUM(t.nDescargas), 0) FROM Thesis t WHERE t.isActive = true"
                    + " AND month(t.publicationDate) = :month"
                    + " AND year(t.publicationDate) = :year")
    Long sumDownloadsForMonth(@Param("month") int month, @Param("year") int year);

    @Query(
            "SELECT ta.authorId, COUNT(ta) FROM ThesisAuthor ta"
                    + " WHERE ta.thesis.isActive = true"
                    + " GROUP BY ta.authorId ORDER BY COUNT(ta) DESC")
    List<Object[]> findTopAuthorIdsWithCount(Pageable pageable);

    @Query(
            "SELECT t.advisorId, COUNT(t) FROM Thesis t"
                    + " WHERE t.isActive = true AND t.advisorId IS NOT NULL"
                    + " GROUP BY t.advisorId ORDER BY COUNT(t) DESC")
    List<Object[]> findAdvisorIdsWithThesisCount();

    @Query(
            "SELECT year(t.publicationDate), COUNT(t) FROM Thesis t"
                    + " WHERE t.isActive = true AND t.publicationDate IS NOT NULL"
                    + " GROUP BY year(t.publicationDate) ORDER BY year(t.publicationDate)")
    List<Object[]> countByPublicationYear();

    @Query(
            "SELECT t.collectionId, COUNT(t) FROM Thesis t"
                    + " WHERE t.isActive = true"
                    + " GROUP BY t.collectionId ORDER BY COUNT(t) DESC")
    List<Object[]> countByCollectionIdGrouped(Pageable pageable);

    @Query(
            value =
                    "SELECT DISTINCT t FROM Thesis t LEFT JOIN t.thesisAuthors ta"
                            + " WHERE t.isActive = true"
                            + " AND (:year IS NULL OR YEAR(t.publicationDate) = :year)"
                            + " AND (cast(:title as String) IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', cast(:title as String), '%')))"
                            + " AND (:authorId IS NULL OR ta.authorId = :authorId)"
                            + " AND (:collectionId IS NULL OR t.collectionId = :collectionId)",
            countQuery =
                    "SELECT COUNT(DISTINCT t) FROM Thesis t LEFT JOIN t.thesisAuthors ta"
                            + " WHERE t.isActive = true"
                            + " AND (:year IS NULL OR YEAR(t.publicationDate) = :year)"
                            + " AND (cast(:title as String) IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', cast(:title as String), '%')))"
                            + " AND (:authorId IS NULL OR ta.authorId = :authorId)"
                            + " AND (:collectionId IS NULL OR t.collectionId = :collectionId)")
    Page<Thesis> search(
            @Param("year") Integer year,
            @Param("title") String title,
            @Param("authorId") Long authorId,
            @Param("collectionId") Long collectionId,
            Pageable pageable);
}
