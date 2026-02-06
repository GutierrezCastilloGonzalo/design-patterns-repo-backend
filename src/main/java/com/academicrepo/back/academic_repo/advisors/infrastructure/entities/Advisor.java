package com.academicrepo.back.academic_repo.advisors.infrastructure.entities;

import com.academicrepo.back.academic_repo.general.entities.repositoryEntities.BaseAbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "advisors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Advisor extends BaseAbstractEntity {

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(length = 255, unique = true)
    private String email;

    @Column(length = 50, unique = true)
    private String orcid;

    @Column(length = 255)
    private String department;

    @Column(length = 100)
    private String title;
}
