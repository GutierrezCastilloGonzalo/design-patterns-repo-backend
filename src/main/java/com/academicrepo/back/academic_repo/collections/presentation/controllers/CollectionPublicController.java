package com.academicrepo.back.academic_repo.collections.presentation.controllers;

import com.academicrepo.back.academic_repo.collections.domain.repositories.ICollectionRepository;
import com.academicrepo.back.academic_repo.collections.presentation.dto.CollectionSelectDto;
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
@RequestMapping("/public/collections")
@RequiredArgsConstructor
@Tag(name = "Public", description = "Endpoints públicos sin autenticación")
public class CollectionPublicController extends BaseV1Controller {

    private final ICollectionRepository repository;

    @GetMapping
    @Operation(summary = "Listar todas las colecciones activas (sin autenticación)")
    public List<CollectionSelectDto> getAll() {
        return repository.findAllActive().stream()
                .map(c -> new CollectionSelectDto(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }
}
