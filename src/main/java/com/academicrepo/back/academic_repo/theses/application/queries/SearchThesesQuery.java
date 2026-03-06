package com.academicrepo.back.academic_repo.theses.application.queries;

public record SearchThesesQuery(
        Integer year,
        String title,
        Long authorId,
        Long collectionId,
        int page,
        int size,
        String sortBy,
        String sortDir) {}
