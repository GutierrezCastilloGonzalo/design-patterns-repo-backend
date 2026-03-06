package com.academicrepo.back.academic_repo.theses.domain.repositories;

import com.academicrepo.back.academic_repo.theses.domain.entities.DThesis;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IThesisRepository {
    DThesis save(DThesis thesis);

    DThesis update(DThesis thesis);

    DThesis findById(Long id);

    Page<DThesis> findAll(Pageable pageConfig);

    Page<DThesis> findByCollectionId(Long collectionId, Pageable pageConfig);

    Page<DThesis> findByAdvisorId(Long advisorId, Pageable pageConfig);

    boolean existsByTitleAndCollectionId(String title, Long collectionId);

    void incrementDownloadCount(Long id);

    void incrementViewCount(Long id);

    List<DThesis> findTopByDownloads(int limit);
}
