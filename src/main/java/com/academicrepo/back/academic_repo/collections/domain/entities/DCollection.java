package com.academicrepo.back.academic_repo.collections.domain.entities;

import com.academicrepo.back.academic_repo.general.entities.domainEntities.BaseAbstractDomainEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DCollection extends BaseAbstractDomainEntity {
    private String name;
    private String description;
    private Long subcommunityId;

    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la coleccion es requerido");
        }
        if (subcommunityId == null) {
            throw new IllegalArgumentException("La subcomunidad padre es requerida");
        }
    }
}
