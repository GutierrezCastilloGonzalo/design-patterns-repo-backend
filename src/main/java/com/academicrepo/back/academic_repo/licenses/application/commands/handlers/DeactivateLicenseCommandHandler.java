package com.academicrepo.back.academic_repo.licenses.application.commands.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.licenses.application.commands.DeactivateLicenseCommand;
import com.academicrepo.back.academic_repo.licenses.domain.entities.DLicense;
import com.academicrepo.back.academic_repo.licenses.domain.repositories.ILicenseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeactivateLicenseCommandHandler {

    private final ILicenseRepository repository;

    @Transactional
    public DLicense execute(DeactivateLicenseCommand command) {
        try {
            DLicense license = repository.findById(command.id());
            if (license == null) {
                throw new IllegalArgumentException(
                        "Licencia no encontrada con id: " + command.id());
            }

            license.setIsActive(false);

            return repository.update(license);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
