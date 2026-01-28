package com.academicrepo.back.academic_repo.theses.application.commands.handlers;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.theses.application.commands.CreateThesisCommand;
import com.academicrepo.back.academic_repo.theses.domain.entities.DThesis;
import com.academicrepo.back.academic_repo.theses.domain.repositories.IThesisRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateThesisCommandHandler {

    private final IThesisRepository repository;

    @Transactional
    public DThesis execute(CreateThesisCommand command) {
        try {
            DThesis thesis = new DThesis();
            thesis.setTitle(command.dto().getTitle());
            thesis.setAbstractText(command.dto().getAbstractText());
            thesis.setPublicationDate(command.dto().getPublicationDate());
            thesis.setFileUrl(command.dto().getFileUrl());
            thesis.setNumberOfPages(command.dto().getNumberOfPages());
            thesis.setLanguage(command.dto().getLanguage());
            thesis.setDocumentType(command.dto().getDocumentType());
            thesis.setDoi(command.dto().getDoi());
            thesis.setLicense(command.dto().getLicense());
            thesis.setCollectionId(command.dto().getCollectionId());
            thesis.setAdvisorId(command.dto().getAdvisorId());
            thesis.setAuthorIds(command.dto().getAuthorIds() != null ? command.dto().getAuthorIds() : new ArrayList<>());
            thesis.setKeywordIds(command.dto().getKeywordIds() != null ? command.dto().getKeywordIds() : new ArrayList<>());

            thesis.validate();

            return repository.save(thesis);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
