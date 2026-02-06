package com.academicrepo.back.academic_repo.users.application.queries.handlers;

import com.academicrepo.back.academic_repo.general.utils.exceptions.HttpExceptionUtils;
import com.academicrepo.back.academic_repo.users.application.queries.GetUserByIdQuery;
import com.academicrepo.back.academic_repo.users.domain.entities.DUser;
import com.academicrepo.back.academic_repo.users.domain.repositories.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class GetUserByIdQueryHandler {
    private final IUserRepository userRepository;

    public GetUserByIdQueryHandler(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DUser execute(GetUserByIdQuery query) {
        try {
            DUser user = userRepository.findById(query.userId());
            if (user == null) {
                throw new IllegalArgumentException(
                        "Usuario no encontrado con ID: " + query.userId());
            }
            return user;
        } catch (Exception e) {
            throw HttpExceptionUtils.processHttpException(e);
        }
    }
}
