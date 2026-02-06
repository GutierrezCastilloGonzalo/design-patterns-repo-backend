package com.academicrepo.back.academic_repo.authors.application.commands.handlers;

import com.academicrepo.back.academic_repo.authors.application.commands.CreateAuthorCommand;
import com.academicrepo.back.academic_repo.authors.domain.entities.DAuthor;
import com.academicrepo.back.academic_repo.authors.domain.repositories.IAuthorRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAuthorCommandHandler {

    private final IAuthorRepository repository;

    @Transactional
    public DAuthor execute(CreateAuthorCommand command) {
        try {
            if (command.dto().getEmail() != null
                    && repository.existsByEmail(command.dto().getEmail())) {
                throw new IllegalArgumentException(
                        "Ya existe un autor con el email: " + command.dto().getEmail());
            }

            if (command.dto().getOrcid() != null
                    && repository.existsByOrcid(command.dto().getOrcid())) {
                throw new IllegalArgumentException(
                        "Ya existe un autor con el ORCID: " + command.dto().getOrcid());
            }

            DAuthor author = new DAuthor();
            author.setFirstName(command.dto().getFirstName());
            author.setLastName(command.dto().getLastName());
            author.setEmail(command.dto().getEmail());
            author.setOrcid(command.dto().getOrcid());
            author.setAffiliation(command.dto().getAffiliation());
            author.validate();

            return repository.save(author);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
