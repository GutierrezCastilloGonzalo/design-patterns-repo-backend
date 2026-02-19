package com.academicrepo.back.academic_repo.policies.domain.entities;

import com.academicrepo.back.academic_repo.general.entities.domainEntities.BaseAbstractDomainEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DAcceptancePolicy extends BaseAbstractDomainEntity {
    private String code;
    private String name;
    private String description;
    private Boolean required;

    public void validate() {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo de la politica es requerido");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la politica es requerido");
        }
    }
}
