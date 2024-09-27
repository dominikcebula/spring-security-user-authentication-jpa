package com.dominikcebula.spring.security.user.authentication.spring.data;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

public class DataIntegrityViolationExceptionUtil {
    public static boolean isConstraintViolation(String constraintName, DataIntegrityViolationException e) {
        if (e.getCause() instanceof ConstraintViolationException constraintViolationException)
            return constraintName.equals(constraintViolationException.getConstraintName());

        return false;
    }
}
