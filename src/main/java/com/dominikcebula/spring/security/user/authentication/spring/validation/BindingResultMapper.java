package com.dominikcebula.spring.security.user.authentication.spring.validation;

import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.BindingResult;

public class BindingResultMapper<E extends Exception> {
    @SuppressWarnings("unchecked")
    public static <R, E extends Exception> R execute(Operation<R> operation, BindingResult bindingResult, R defaultResult) throws E {
        try {
            return operation.execute();
        } catch (ConstraintViolationException e) {
            ConstraintViolationProcessor.addErrors(e, bindingResult);
            return defaultResult;
        } catch (Exception e) {
            throw (E) e;
        }
    }

    @FunctionalInterface
    public interface Operation<R> {
        R execute() throws Exception;
    }
}
