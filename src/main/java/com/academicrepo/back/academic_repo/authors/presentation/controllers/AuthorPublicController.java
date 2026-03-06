package com.academicrepo.back.academic_repo.authors.presentation.controllers;

import com.academicrepo.back.academic_repo.authors.domain.repositories.IAuthorRepository;
import com.academicrepo.back.academic_repo.authors.presentation.dto.AuthorSelectDto;
import com.academicrepo.back.academic_repo.general.presentation.controllers.BaseV1Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/authors")
@RequiredArgsConstructor
@Tag(name = "Public", description = "Endpoints públicos sin autenticación")
public class AuthorPublicController extends BaseV1Controller {

    private final IAuthorRepository repository;

    @GetMapping
    @Operation(summary = "Listar todos los autores activos (sin autenticación)")
    public List<AuthorSelectDto> getAll() {
        return repository.findAllActive().stream()
                .map(a -> new AuthorSelectDto(a.getId(), a.getFirstName() + " " + a.getLastName()))
                .collect(Collectors.toList());
    }
}
