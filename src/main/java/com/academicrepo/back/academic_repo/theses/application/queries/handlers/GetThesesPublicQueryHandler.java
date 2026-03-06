package com.academicrepo.back.academic_repo.theses.application.queries.handlers;

import com.academicrepo.back.academic_repo.authors.domain.entities.DAuthor;
import com.academicrepo.back.academic_repo.authors.domain.repositories.IAuthorRepository;
import com.academicrepo.back.academic_repo.collections.domain.entities.DCollection;
import com.academicrepo.back.academic_repo.collections.domain.repositories.ICollectionRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.keywords.domain.repositories.IKeywordRepository;
import com.academicrepo.back.academic_repo.theses.application.queries.GetThesesPublicQuery;
import com.academicrepo.back.academic_repo.theses.domain.repositories.IThesisRepository;
import com.academicrepo.back.academic_repo.theses.presentation.dto.ThesisPublicDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetThesesPublicQueryHandler {

    private final IThesisRepository thesisRepository;
    private final IAuthorRepository authorRepository;
    private final ICollectionRepository collectionRepository;
    private final IKeywordRepository keywordRepository;

    public Page<ThesisPublicDto> execute(GetThesesPublicQuery query) {
        try {
            Sort sort =
                    query.sortDir().equalsIgnoreCase("desc")
                            ? Sort.by(query.sortBy()).descending()
                            : Sort.by(query.sortBy()).ascending();

            return thesisRepository
                    .findAll(PageRequest.of(Math.max(0, query.page() - 1), query.size(), sort))
                    .map(
                            thesis -> {
                                DCollection collection =
                                        collectionRepository.findById(thesis.getCollectionId());
                                String collectionName =
                                        collection != null ? collection.getName() : null;

                                List<String> authorNames =
                                        authorRepository
                                                .findAllByIds(thesis.getAuthorIds())
                                                .stream()
                                                .map(
                                                        a ->
                                                                (a.getFirstName()
                                                                                + " "
                                                                                + a.getLastName())
                                                                        .trim())
                                                .collect(Collectors.toList());

                                List<String> keywordWords =
                                        keywordRepository
                                                .findAllByIds(thesis.getKeywordIds())
                                                .stream()
                                                .map(k -> k.getWord())
                                                .collect(Collectors.toList());

                                return ThesisPublicDto.builder()
                                        .id(thesis.getId())
                                        .isActive(thesis.getIsActive())
                                        .createdDate(thesis.getCreatedDate())
                                        .updatedDate(thesis.getUpdatedDate())
                                        .title(thesis.getTitle())
                                        .abstractText(thesis.getAbstractText())
                                        .publicationDate(thesis.getPublicationDate())
                                        .fileUrl(thesis.getFileUrl())
                                        .numberOfPages(thesis.getNumberOfPages())
                                        .language(thesis.getLanguage())
                                        .documentType(thesis.getDocumentType())
                                        .doi(thesis.getDoi())
                                        .license(thesis.getLicense())
                                        .collection(collectionName)
                                        .authors(authorNames)
                                        .keywords(keywordWords)
                                        .nDescargas(thesis.getNDescargas())
                                        .nVistas(thesis.getNVistas())
                                        .build();
                            });
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
