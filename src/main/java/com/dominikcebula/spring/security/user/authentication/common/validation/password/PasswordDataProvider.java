package com.dominikcebula.spring.security.user.authentication.common.validation.password;

public interface PasswordDataProvider {
    String getPassword();

    String getRepeatedPassword();
}
