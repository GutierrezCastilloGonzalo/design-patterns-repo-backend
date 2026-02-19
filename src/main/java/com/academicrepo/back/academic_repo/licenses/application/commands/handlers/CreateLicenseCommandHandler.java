package com.academicrepo.back.academic_repo.licenses.application.commands.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.licenses.application.commands.CreateLicenseCommand;
import com.academicrepo.back.academic_repo.licenses.domain.entities.DLicense;
import com.academicrepo.back.academic_repo.licenses.domain.entities.DPolicyAcceptance;
import com.academicrepo.back.academic_repo.licenses.domain.repositories.ILicenseRepository;
import com.academicrepo.back.academic_repo.licenses.presentation.dto.PolicyAcceptanceDto;
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
public class CreateLicenseCommandHandler {

    private final ILicenseRepository repository;
    private final StorageService storageService;

    @Transactional
    public DLicense execute(CreateLicenseCommand command) {
        try {
            if (repository.existsByName(command.dto().getName())) {
                throw new IllegalArgumentException(
                        "Ya existe una licencia con el nombre: " + command.dto().getName());
            }

            // Upload file if provided
            String fileUrl = command.dto().getFileUrl();
            MultipartFile file = command.file();
            if (file != null && !file.isEmpty() && storageService.isAvailable()) {
                FileUploadResponse uploadResponse = storageService.uploadFile(file, "licenses");
                fileUrl = uploadResponse.getFileUrl();
            }

            DLicense license = new DLicense();
            license.setName(command.dto().getName());
            license.setDescription(command.dto().getDescription());
            license.setType(command.dto().getType());
            license.setStatus("PENDING");
            license.setVersion(command.dto().getVersion());
            license.setFileUrl(fileUrl);
            license.setValidFrom(command.dto().getValidFrom());
            license.setValidUntil(command.dto().getValidUntil());

            // Map policy acceptances
            if (command.dto().getPolicyAcceptances() != null) {
                List<DPolicyAcceptance> acceptances = new ArrayList<>();
                for (PolicyAcceptanceDto dto : command.dto().getPolicyAcceptances()) {
                    DPolicyAcceptance dpa = new DPolicyAcceptance();
                    dpa.setPolicyId(dto.getPolicyId());
                    dpa.setAccepted(dto.getAccepted());
                    if (dto.getAccepted()) {
                        dpa.setAcceptedAt(LocalDateTime.now());
                    }
                    acceptances.add(dpa);
                }
                license.setPolicyAcceptances(acceptances);
            }

            license.validate();

            return repository.save(license);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
