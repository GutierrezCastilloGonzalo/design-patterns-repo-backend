package com.academicrepo.back.academic_repo.licenses.application.queries.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.licenses.application.queries.GetLicenseByIdQuery;
import com.academicrepo.back.academic_repo.licenses.domain.entities.DLicense;
import com.academicrepo.back.academic_repo.licenses.domain.repositories.ILicenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetLicenseByIdQueryHandler {

    private final ILicenseRepository repository;

    public DLicense execute(GetLicenseByIdQuery query) {
        try {
            DLicense license = repository.findById(query.id());
            if (license == null) {
                throw new IllegalArgumentException("Licencia no encontrada con id: " + query.id());
            }
            return license;
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
