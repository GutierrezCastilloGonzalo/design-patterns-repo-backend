package com.academicrepo.back.academic_repo.licenses.infrastructure.mappers;

import com.academicrepo.back.academic_repo.licenses.domain.entities.DLicense;
import com.academicrepo.back.academic_repo.licenses.domain.entities.DPolicyAcceptance;
import com.academicrepo.back.academic_repo.licenses.infrastructure.entities.License;
import com.academicrepo.back.academic_repo.licenses.infrastructure.entities.LicensePolicyAcceptance;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LicenseMapper {

    public License toPersistence(DLicense domain) {
        if (domain == null) return null;

        License.LicenseBuilder<?, ?> builder = License.builder();
        builder.name(domain.getName())
                .description(domain.getDescription())
                .type(domain.getType())
                .status(domain.getStatus())
                .version(domain.getVersion())
                .fileUrl(domain.getFileUrl())
                .validFrom(domain.getValidFrom())
                .validUntil(domain.getValidUntil());

        if (domain.getId() != null) builder.id(domain.getId());
        if (domain.getIsActive() != null) builder.isActive(domain.getIsActive());
        if (domain.getCreatedDate() != null) builder.createdDate(domain.getCreatedDate());
        if (domain.getUpdatedDate() != null) builder.updatedDate(domain.getUpdatedDate());

        License license = builder.build();

        // Map policy acceptances
        if (domain.getPolicyAcceptances() != null && !domain.getPolicyAcceptances().isEmpty()) {
            List<LicensePolicyAcceptance> acceptances = new ArrayList<>();
            for (DPolicyAcceptance dpa : domain.getPolicyAcceptances()) {
                LicensePolicyAcceptance lpa =
                        LicensePolicyAcceptance.builder()
                                .id(dpa.getId())
                                .license(license)
                                .policyId(dpa.getPolicyId())
                                .accepted(dpa.getAccepted())
                                .acceptedAt(dpa.getAcceptedAt())
                                .build();
                acceptances.add(lpa);
            }
            license.setPolicyAcceptances(acceptances);
        }

        return license;
    }

    public DLicense toDomain(License entity) {
        if (entity == null) return null;

        DLicense domain = new DLicense();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setDescription(entity.getDescription());
        domain.setType(entity.getType());
        domain.setStatus(entity.getStatus());
        domain.setVersion(entity.getVersion());
        domain.setFileUrl(entity.getFileUrl());
        domain.setValidFrom(entity.getValidFrom());
        domain.setValidUntil(entity.getValidUntil());
        domain.setIsActive(entity.getIsActive());
        domain.setCreatedDate(entity.getCreatedDate());
        domain.setUpdatedDate(entity.getUpdatedDate());

        // Map policy acceptances
        if (entity.getPolicyAcceptances() != null && !entity.getPolicyAcceptances().isEmpty()) {
            List<DPolicyAcceptance> acceptances = new ArrayList<>();
            for (LicensePolicyAcceptance lpa : entity.getPolicyAcceptances()) {
                DPolicyAcceptance dpa = new DPolicyAcceptance();
                dpa.setId(lpa.getId());
                dpa.setPolicyId(lpa.getPolicyId());
                dpa.setAccepted(lpa.getAccepted());
                dpa.setAcceptedAt(lpa.getAcceptedAt());
                acceptances.add(dpa);
            }
            domain.setPolicyAcceptances(acceptances);
        }

        return domain;
    }
}
