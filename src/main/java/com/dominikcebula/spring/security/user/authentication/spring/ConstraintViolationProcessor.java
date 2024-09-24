package com.dominikcebula.spring.security.user.authentication.spring;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.Set;

class ConstraintViolationProcessor {
    private static final ValidatorAdapter VALIDATOR_ADAPTER = new ValidatorAdapter();

    static void addErrors(ConstraintViolationException constraintViolations, BindingResult bindingResult) {
        VALIDATOR_ADAPTER.addConstraintViolations(constraintViolations.getConstraintViolations(), bindingResult);
    }

    private static class ValidatorAdapter extends SpringValidatorAdapter {
        private ValidatorAdapter() {
            super(new CustomValidatorBean());
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private void addConstraintViolations(Set<ConstraintViolation<?>> violations, BindingResult bindingResult) {
            processConstraintViolations((Set) violations, bindingResult);
        }
    }
}
