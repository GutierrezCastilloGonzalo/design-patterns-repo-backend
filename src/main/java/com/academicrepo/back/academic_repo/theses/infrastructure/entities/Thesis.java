package com.academicrepo.back.academic_repo.theses.infrastructure.entities;

import com.academicrepo.back.academic_repo.general.entities.repositoryEntities.BaseAbstractEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "theses",
        indexes = {
            @Index(name = "idx_theses_collection_id", columnList = "collection_id"),
            @Index(name = "idx_theses_advisor_id", columnList = "advisor_id"),
            @Index(name = "idx_theses_publication_date", columnList = "publication_date")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Thesis extends BaseAbstractEntity {

    @Column(nullable = false, length = 500)
    private String title;

    @Column(name = "abstract_text", columnDefinition = "TEXT")
    private String abstractText;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @Column(name = "number_of_pages")
    private Integer numberOfPages;

    @Column(length = 50)
    private String language;

    @Column(name = "document_type", length = 100)
    private String documentType;

    @Column(length = 100)
    private String doi;

    @Column(length = 100)
    private String license;

    @Column(name = "collection_id", nullable = false)
    private Long collectionId;

    @Column(name = "advisor_id")
    private Long advisorId;

    @lombok.Builder.Default
    @OneToMany(
            mappedBy = "thesis",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<ThesisAuthor> thesisAuthors = new HashSet<>();

    @lombok.Builder.Default
    @OneToMany(
            mappedBy = "thesis",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<KeyWordThesis> keyWordTheses = new HashSet<>();
}
