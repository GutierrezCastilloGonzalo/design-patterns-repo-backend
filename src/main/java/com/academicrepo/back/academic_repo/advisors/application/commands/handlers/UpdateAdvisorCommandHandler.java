package com.academicrepo.back.academic_repo.advisors.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.academicrepo.back.academic_repo.advisors.application.commands.UpdateAdvisorCommand;
import com.academicrepo.back.academic_repo.advisors.domain.entities.DAdvisor;
import com.academicrepo.back.academic_repo.advisors.domain.repositories.IAdvisorRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateAdvisorCommandHandler {

    private final IAdvisorRepository repository;

    @Transactional
    public DAdvisor execute(UpdateAdvisorCommand command) {
        try {
            DAdvisor existing = repository.findById(command.id());
            if (existing == null) {
                throw new IllegalArgumentException("Asesor no encontrado con ID: " + command.id());
            }

            if (command.dto().getFirstName() != null) {
                existing.setFirstName(command.dto().getFirstName());
            }
            if (command.dto().getLastName() != null) {
                existing.setLastName(command.dto().getLastName());
            }
            if (command.dto().getEmail() != null) {
                if (!command.dto().getEmail().equals(existing.getEmail()) && repository.existsByEmail(command.dto().getEmail())) {
                    throw new IllegalArgumentException("Ya existe un asesor con el email: " + command.dto().getEmail());
                }
                existing.setEmail(command.dto().getEmail());
            }
            if (command.dto().getOrcid() != null) {
                if (!command.dto().getOrcid().equals(existing.getOrcid()) && repository.existsByOrcid(command.dto().getOrcid())) {
                    throw new IllegalArgumentException("Ya existe un asesor con el ORCID: " + command.dto().getOrcid());
                }
                existing.setOrcid(command.dto().getOrcid());
            }
            if (command.dto().getDepartment() != null) {
                existing.setDepartment(command.dto().getDepartment());
            }
            if (command.dto().getTitle() != null) {
                existing.setTitle(command.dto().getTitle());
            }

            existing.validate();
            return repository.update(existing);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
