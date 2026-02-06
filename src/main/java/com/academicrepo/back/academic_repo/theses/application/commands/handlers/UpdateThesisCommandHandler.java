package com.academicrepo.back.academic_repo.theses.application.commands.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.theses.application.commands.UpdateThesisCommand;
import com.academicrepo.back.academic_repo.theses.domain.entities.DThesis;
import com.academicrepo.back.academic_repo.theses.domain.repositories.IThesisRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateThesisCommandHandler {

    private final IThesisRepository repository;

    @Transactional
    public DThesis execute(UpdateThesisCommand command) {
        try {
            DThesis existing = repository.findById(command.id());
            if (existing == null) {
                throw new IllegalArgumentException("Tesis no encontrada con ID: " + command.id());
            }

            if (command.dto().getTitle() != null) {
                existing.setTitle(command.dto().getTitle());
            }
            if (command.dto().getAbstractText() != null) {
                existing.setAbstractText(command.dto().getAbstractText());
            }
            if (command.dto().getPublicationDate() != null) {
                existing.setPublicationDate(command.dto().getPublicationDate());
            }
            if (command.dto().getFileUrl() != null) {
                existing.setFileUrl(command.dto().getFileUrl());
            }
            if (command.dto().getNumberOfPages() != null) {
                existing.setNumberOfPages(command.dto().getNumberOfPages());
            }
            if (command.dto().getLanguage() != null) {
                existing.setLanguage(command.dto().getLanguage());
            }
            if (command.dto().getDocumentType() != null) {
                existing.setDocumentType(command.dto().getDocumentType());
            }
            if (command.dto().getDoi() != null) {
                existing.setDoi(command.dto().getDoi());
            }
            if (command.dto().getLicense() != null) {
                existing.setLicense(command.dto().getLicense());
            }
            if (command.dto().getAdvisorId() != null) {
                existing.setAdvisorId(command.dto().getAdvisorId());
            }
            if (command.dto().getAuthorIds() != null) {
                existing.setAuthorIds(command.dto().getAuthorIds());
            }
            if (command.dto().getKeywordIds() != null) {
                existing.setKeywordIds(command.dto().getKeywordIds());
            }

            existing.validate();
            return repository.update(existing);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
