package com.academicrepo.back.academic_repo.collections.infrastructure.entities;

import com.academicrepo.back.academic_repo.general.entities.repositoryEntities.BaseAbstractEntity;
import com.academicrepo.back.academic_repo.subcommunities.infrastructure.entities.Subcommunity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "collections")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Collection extends BaseAbstractEntity {

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "subcommunity_id", nullable = false)
    private Long subcommunityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcommunity_id", insertable = false, updatable = false)
    private Subcommunity subcommunity;
}
