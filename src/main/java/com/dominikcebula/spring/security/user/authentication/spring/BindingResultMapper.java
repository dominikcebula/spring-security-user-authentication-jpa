package com.dominikcebula.spring.security.user.authentication.spring;

import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.BindingResult;

public class BindingResultMapper<E extends Exception> {
    @SuppressWarnings("unchecked")
    public static <E extends Exception> void execute(Operation operation, BindingResult bindingResult) throws E {
        try {
            operation.execute();
        } catch (ConstraintViolationException e) {
            ConstraintViolationProcessor.addErrors(e, bindingResult);
        } catch (Exception e) {
            throw (E) e;
        }
    }

    @FunctionalInterface
    public interface Operation {
        void execute() throws Exception;
    }
}
