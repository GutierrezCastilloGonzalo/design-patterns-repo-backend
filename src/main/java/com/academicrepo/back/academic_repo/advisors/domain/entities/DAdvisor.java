package com.academicrepo.back.academic_repo.advisors.domain.entities;

import com.academicrepo.back.academic_repo.general.entities.domainEntities.BaseAbstractDomainEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DAdvisor extends BaseAbstractDomainEntity {
    private String firstName;
    private String lastName;
    private String email;
    private String orcid;
    private String department;
    private String title;

    public void validate() {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del asesor es requerido");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido del asesor es requerido");
        }
    }
}
