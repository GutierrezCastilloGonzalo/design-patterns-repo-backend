package com.academicrepo.back.academic_repo.users.application.queries.handlers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.users.application.queries.UserPaginatedQuery;
import com.academicrepo.back.academic_repo.users.domain.entities.DUser;
import com.academicrepo.back.academic_repo.users.domain.repositories.IUserRepository;
import com.academicrepo.back.academic_repo.users.presentation.dto.PaginatedUserRequestDto;

@Service
public class UserPaginatedQueryHandler {
    private final IUserRepository userRepository;

    public UserPaginatedQueryHandler(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<DUser> execute(UserPaginatedQuery query) {
        try {
            PaginatedUserRequestDto requestDto = query.requestDto();

            Sort sort = requestDto.getSortDirection().equalsIgnoreCase("desc")
                ? Sort.by(requestDto.getSortBy()).descending()
                : Sort.by(requestDto.getSortBy()).ascending();

            Pageable pageable = PageRequest.of(
                requestDto.getPage(),
                requestDto.getSize(),
                sort
            );

            return userRepository.findAll(pageable);
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
