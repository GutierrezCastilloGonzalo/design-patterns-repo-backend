package com.academicrepo.back.academic_repo.auth.application.queries.handlers;

import com.academicrepo.back.academic_repo.auth.application.queries.GetCurrentUserQuery;
import com.academicrepo.back.academic_repo.auth.presentation.dto.AuthResponseDto;
import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.users.domain.entities.DUser;
import com.academicrepo.back.academic_repo.users.domain.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCurrentUserQueryHandler {

    private final IUserRepository userRepository;

    public AuthResponseDto.UserInfoDto execute(GetCurrentUserQuery query) {
        try {
            DUser user = userRepository.findById(query.userId());
            if (user == null) {
                throw new IllegalArgumentException("Usuario no encontrado");
            }

            return AuthResponseDto.UserInfoDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .userName(user.getUserName().orElse(null))
                    .build();
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
