package com.dominikcebula.spring.security.user.authentication.signup.validation;

public interface PasswordDataProvider {
    String getPassword();

    String getRepeatedPassword();
}
