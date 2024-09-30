package com.dominikcebula.spring.security.user.authentication.common.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, PasswordDataProvider> {
    public boolean isValid(PasswordDataProvider passwordDataProvider, ConstraintValidatorContext constraintValidatorContext) {
        String password = passwordDataProvider.getPassword();
        String repeatPassword = passwordDataProvider.getRepeatedPassword();

        return StringUtils.equals(password, repeatPassword);
    }
}
