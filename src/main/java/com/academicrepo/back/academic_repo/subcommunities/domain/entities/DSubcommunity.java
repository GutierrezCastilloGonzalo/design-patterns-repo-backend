package com.academicrepo.back.academic_repo.subcommunities.domain.entities;

import com.academicrepo.back.academic_repo.general.entities.domainEntities.BaseAbstractDomainEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DSubcommunity extends BaseAbstractDomainEntity {
    private String name;
    private String description;
    private Long communityId;

    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la subcomunidad es requerido");
        }
        if (communityId == null) {
            throw new IllegalArgumentException("La comunidad padre es requerida");
        }
    }
}
