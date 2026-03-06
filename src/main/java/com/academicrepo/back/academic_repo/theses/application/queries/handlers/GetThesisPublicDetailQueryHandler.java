package com.academicrepo.back.academic_repo.theses.application.queries.handlers;

import com.academicrepo.back.academic_repo.authors.domain.repositories.IAuthorRepository;
import com.academicrepo.back.academic_repo.collections.domain.entities.DCollection;
import com.academicrepo.back.academic_repo.collections.domain.repositories.ICollectionRepository;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.keywords.domain.repositories.IKeywordRepository;
import com.academicrepo.back.academic_repo.theses.application.queries.GetThesisPublicDetailQuery;
import com.academicrepo.back.academic_repo.theses.domain.entities.DThesis;
import com.academicrepo.back.academic_repo.theses.domain.repositories.IThesisRepository;
import com.academicrepo.back.academic_repo.theses.presentation.dto.ThesisPublicDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetThesisPublicDetailQueryHandler {

    private final IThesisRepository thesisRepository;
    private final IAuthorRepository authorRepository;
    private final ICollectionRepository collectionRepository;
    private final IKeywordRepository keywordRepository;

    public ThesisPublicDto execute(GetThesisPublicDetailQuery query) {
        try {
            DThesis thesis = thesisRepository.findById(query.id());
            if (thesis == null) {
                throw new IllegalArgumentException("Tesis no encontrada con ID: " + query.id());
            }

            DCollection collection = collectionRepository.findById(thesis.getCollectionId());
            String collectionName = collection != null ? collection.getName() : null;

            List<String> authorNames =
                    authorRepository.findAllByIds(thesis.getAuthorIds()).stream()
                            .map(a -> (a.getFirstName() + " " + a.getLastName()).trim())
                            .collect(Collectors.toList());

            List<String> keywordWords =
                    keywordRepository.findAllByIds(thesis.getKeywordIds()).stream()
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
                    .thumbnailUrl(thesis.getThumbnailUrl())
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
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
