package com.academicrepo.back.academic_repo.theses.application.queries;

public record GetThesesPublicQuery(int page, int size, String sortBy, String sortDir) {}
