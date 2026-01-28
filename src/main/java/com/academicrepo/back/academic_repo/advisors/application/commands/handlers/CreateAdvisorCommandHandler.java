package com.academicrepo.back.academic_repo.advisors.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.academicrepo.back.academic_repo.advisors.application.commands.CreateAdvisorCommand;
import com.academicrepo.back.academic_repo.advisors.domain.entities.DAdvisor;
import com.academicrepo.back.academic_repo.advisors.domain.repositories.IAdvisorRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateAdvisorCommandHandler {

    private final IAdvisorRepository repository;

    @Transactional
    public DAdvisor execute(CreateAdvisorCommand command) {
        try {
            if (command.dto().getEmail() != null && repository.existsByEmail(command.dto().getEmail())) {
                throw new IllegalArgumentException("Ya existe un asesor con el email: " + command.dto().getEmail());
            }

            if (command.dto().getOrcid() != null && repository.existsByOrcid(command.dto().getOrcid())) {
                throw new IllegalArgumentException("Ya existe un asesor con el ORCID: " + command.dto().getOrcid());
            }

            DAdvisor advisor = new DAdvisor();
            advisor.setFirstName(command.dto().getFirstName());
            advisor.setLastName(command.dto().getLastName());
            advisor.setEmail(command.dto().getEmail());
            advisor.setOrcid(command.dto().getOrcid());
            advisor.setDepartment(command.dto().getDepartment());
            advisor.setTitle(command.dto().getTitle());
            advisor.validate();

            return repository.save(advisor);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
