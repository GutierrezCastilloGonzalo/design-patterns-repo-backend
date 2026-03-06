package com.academicrepo.back.academic_repo.theses.application.queries.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.theses.application.queries.GetTopDownloadedThesesQuery;
import com.academicrepo.back.academic_repo.theses.domain.repositories.IThesisRepository;
import com.academicrepo.back.academic_repo.theses.presentation.dto.ThesisTopDownloadedDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTopDownloadedThesesQueryHandler {

    private final IThesisRepository repository;

    public List<ThesisTopDownloadedDto> execute(GetTopDownloadedThesesQuery query) {
        try {
            return repository.findTopByDownloads(query.limit()).stream()
                    .map(
                            thesis ->
                                    ThesisTopDownloadedDto.builder()
                                            .id(String.valueOf(thesis.getId()))
                                            .title(thesis.getTitle())
                                            .nDescargas(thesis.getNDescargas())
                                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
