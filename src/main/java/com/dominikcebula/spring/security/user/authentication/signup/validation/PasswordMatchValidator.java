package com.dominikcebula.spring.security.user.authentication.signup.validation;

import com.dominikcebula.spring.security.user.authentication.signup.SignupData;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, SignupData> {
    public boolean isValid(SignupData signupData, ConstraintValidatorContext constraintValidatorContext) {
        String password = signupData.getPassword();
        String repeatPassword = signupData.getRepeatedPassword();

        return StringUtils.equals(password, repeatPassword);
    }
}
