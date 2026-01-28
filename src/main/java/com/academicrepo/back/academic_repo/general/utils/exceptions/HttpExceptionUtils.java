package com.academicrepo.back.academic_repo.general.utils.exceptions;

import java.util.concurrent.TimeoutException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import com.academicrepo.back.academic_repo.general.entities.domainEntities.GeneralException;

public class HttpExceptionUtils {

    public static GeneralException processHttpException(Exception e) {
        if (e instanceof NullPointerException nullPointerException) {
            return new GeneralException("Se intentó acceder a un objeto nulo.", HttpStatus.INTERNAL_SERVER_ERROR, nullPointerException);
        }

        if (e instanceof IllegalArgumentException || e instanceof IllegalStateException) {
            return new GeneralException(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        }

        if (e instanceof EmptyResultDataAccessException) {
            return new GeneralException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        }

        if (e instanceof DataIntegrityViolationException || e instanceof OptimisticLockingFailureException) {
            return new GeneralException(e.getMessage(), HttpStatus.CONFLICT, e);
        }

        if (e instanceof AuthenticationException) {
            return new GeneralException(e.getMessage(), HttpStatus.UNAUTHORIZED, e);
        }

        if (e instanceof AccessDeniedException) {
            return new GeneralException(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }

        if (e instanceof TimeoutException) {
            return new GeneralException("Tiempo de espera agotado.", HttpStatus.GATEWAY_TIMEOUT, e);
        }

        if (e instanceof GeneralException generalException) {
            return generalException;
        }

        return new GeneralException("Ocurrió un error desconocido.", HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
}
