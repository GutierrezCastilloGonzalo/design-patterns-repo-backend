package com.academicrepo.back.academic_repo.keywords.domain.entities;

import com.academicrepo.back.academic_repo.general.entities.domainEntities.BaseAbstractDomainEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DKeyword extends BaseAbstractDomainEntity {
    private String word;

    public void validate() {
        if (word == null || word.trim().isEmpty()) {
            throw new IllegalArgumentException("La palabra clave es requerida");
        }
    }
}
