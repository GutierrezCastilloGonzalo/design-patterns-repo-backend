package com.academicrepo.back.academic_repo.theses.application.commands.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.theses.application.commands.IncrementDownloadCountCommand;
import com.academicrepo.back.academic_repo.theses.domain.repositories.IThesisRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncrementDownloadCountCommandHandler {

    private final IThesisRepository repository;

    @Transactional
    public void execute(IncrementDownloadCountCommand command) {
        try {
            repository.incrementDownloadCount(command.thesisId());
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
