package com.academicrepo.back.academic_repo.authors.application.commands.handlers;

import com.academicrepo.back.academic_repo.authors.application.commands.UpdateAuthorCommand;
import com.academicrepo.back.academic_repo.authors.domain.entities.DAuthor;
import com.academicrepo.back.academic_repo.authors.domain.repositories.IAuthorRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateAuthorCommandHandler {

    private final IAuthorRepository repository;

    @Transactional
    public DAuthor execute(UpdateAuthorCommand command) {
        try {
            DAuthor existing = repository.findById(command.id());
            if (existing == null) {
                throw new IllegalArgumentException("Autor no encontrado con ID: " + command.id());
            }

            if (command.dto().getFirstName() != null) {
                existing.setFirstName(command.dto().getFirstName());
            }
            if (command.dto().getLastName() != null) {
                existing.setLastName(command.dto().getLastName());
            }
            if (command.dto().getEmail() != null) {
                if (!command.dto().getEmail().equals(existing.getEmail())
                        && repository.existsByEmail(command.dto().getEmail())) {
                    throw new IllegalArgumentException(
                            "Ya existe un autor con el email: " + command.dto().getEmail());
                }
                existing.setEmail(command.dto().getEmail());
            }
            if (command.dto().getOrcid() != null) {
                if (!command.dto().getOrcid().equals(existing.getOrcid())
                        && repository.existsByOrcid(command.dto().getOrcid())) {
                    throw new IllegalArgumentException(
                            "Ya existe un autor con el ORCID: " + command.dto().getOrcid());
                }
                existing.setOrcid(command.dto().getOrcid());
            }
            if (command.dto().getAffiliation() != null) {
                existing.setAffiliation(command.dto().getAffiliation());
            }

            existing.validate();
            return repository.update(existing);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
