package com.academicrepo.back.academic_repo.licenses.domain.entities;

import com.academicrepo.back.academic_repo.general.entities.domainEntities.BaseAbstractDomainEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DLicense extends BaseAbstractDomainEntity {
    private String name;
    private String description;
    private String type;
    private String status;
    private String version;
    private String fileUrl;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private List<DPolicyAcceptance> policyAcceptances = new ArrayList<>();

    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la licencia es requerido");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de licencia es requerido");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado de la licencia es requerido");
        }
    }
}
