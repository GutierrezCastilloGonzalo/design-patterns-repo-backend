package com.academicrepo.back.academic_repo.licenses.domain.entities;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DPolicyAcceptance {
    private Long id;
    private Long policyId;
    private Boolean accepted;
    private LocalDateTime acceptedAt;
}
