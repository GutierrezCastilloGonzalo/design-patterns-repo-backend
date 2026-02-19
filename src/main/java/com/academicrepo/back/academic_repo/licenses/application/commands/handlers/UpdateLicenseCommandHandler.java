package com.academicrepo.back.academic_repo.licenses.application.commands.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.licenses.application.commands.UpdateLicenseCommand;
import com.academicrepo.back.academic_repo.licenses.domain.entities.DLicense;
import com.academicrepo.back.academic_repo.licenses.domain.entities.DPolicyAcceptance;
import com.academicrepo.back.academic_repo.licenses.domain.repositories.ILicenseRepository;
import com.academicrepo.back.academic_repo.licenses.presentation.dto.PolicyAcceptanceDto;
import com.academicrepo.back.academic_repo.licenses.presentation.dto.UpdateLicenseDto;
import com.academicrepo.back.academic_repo.storage.application.StorageService;
import com.academicrepo.back.academic_repo.storage.presentation.dto.FileUploadResponse;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UpdateLicenseCommandHandler {

    private final ILicenseRepository repository;
    private final StorageService storageService;

    @Transactional
    public DLicense execute(UpdateLicenseCommand command) {
        try {
            DLicense license = repository.findById(command.id());
            if (license == null) {
                throw new IllegalArgumentException(
                        "Licencia no encontrada con id: " + command.id());
            }

            UpdateLicenseDto dto = command.dto();

            // Upload file if provided
            MultipartFile file = command.file();
            if (file != null && !file.isEmpty() && storageService.isAvailable()) {
                FileUploadResponse uploadResponse = storageService.uploadFile(file, "licenses");
                license.setFileUrl(uploadResponse.getFileUrl());
            } else if (dto.getFileUrl() != null) {
                license.setFileUrl(dto.getFileUrl());
            }

            if (dto.getName() != null) license.setName(dto.getName());
            if (dto.getDescription() != null) license.setDescription(dto.getDescription());
            if (dto.getType() != null) license.setType(dto.getType());
            if (dto.getStatus() != null) license.setStatus(dto.getStatus());
            if (dto.getVersion() != null) license.setVersion(dto.getVersion());
            if (dto.getValidFrom() != null) license.setValidFrom(dto.getValidFrom());
            if (dto.getValidUntil() != null) license.setValidUntil(dto.getValidUntil());

            // Update policy acceptances if provided
            if (dto.getPolicyAcceptances() != null) {
                List<DPolicyAcceptance> acceptances = new ArrayList<>();
                for (PolicyAcceptanceDto paDto : dto.getPolicyAcceptances()) {
                    DPolicyAcceptance dpa = new DPolicyAcceptance();
                    dpa.setPolicyId(paDto.getPolicyId());
                    dpa.setAccepted(paDto.getAccepted());
                    if (paDto.getAccepted()) {
                        dpa.setAcceptedAt(LocalDateTime.now());
                    }
                    acceptances.add(dpa);
                }
                license.setPolicyAcceptances(acceptances);
            }

            license.validate();

            return repository.update(license);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
