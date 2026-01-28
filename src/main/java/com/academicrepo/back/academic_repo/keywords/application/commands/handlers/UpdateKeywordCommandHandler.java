package com.academicrepo.back.academic_repo.keywords.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.keywords.application.commands.UpdateKeywordCommand;
import com.academicrepo.back.academic_repo.keywords.domain.entities.DKeyword;
import com.academicrepo.back.academic_repo.keywords.domain.repositories.IKeywordRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateKeywordCommandHandler {

    private final IKeywordRepository repository;

    @Transactional
    public DKeyword execute(UpdateKeywordCommand command) {
        try {
            DKeyword existing = repository.findById(command.id());
            if (existing == null) {
                throw new IllegalArgumentException("Palabra clave no encontrada con ID: " + command.id());
            }

            if (command.dto().getWord() != null) {
                if (!existing.getWord().equals(command.dto().getWord()) && repository.existsByWord(command.dto().getWord())) {
                    throw new IllegalArgumentException("Ya existe una palabra clave con el valor: " + command.dto().getWord());
                }
                existing.setWord(command.dto().getWord());
            }

            existing.validate();
            return repository.update(existing);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
