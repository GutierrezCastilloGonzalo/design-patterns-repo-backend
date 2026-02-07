package com.academicrepo.back.academic_repo.keywords.application.commands.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.keywords.application.commands.CreateKeywordCommand;
import com.academicrepo.back.academic_repo.keywords.domain.entities.DKeyword;
import com.academicrepo.back.academic_repo.keywords.domain.repositories.IKeywordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateKeywordCommandHandler {

    private final IKeywordRepository repository;

    @Transactional
    public DKeyword execute(CreateKeywordCommand command) {
        try {
            if (repository.existsByWord(command.dto().getWord())) {
                throw new IllegalArgumentException(
                        "Ya existe una palabra clave con el valor: " + command.dto().getWord());
            }

            DKeyword keyword = new DKeyword();
            keyword.setWord(command.dto().getWord());
            keyword.validate();

            return repository.save(keyword);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
