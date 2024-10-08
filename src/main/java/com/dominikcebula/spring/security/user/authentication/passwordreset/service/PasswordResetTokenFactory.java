package com.dominikcebula.spring.security.user.authentication.passwordreset.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PasswordResetTokenFactory {
    public String create() {
        return UUID.randomUUID().toString();
    }
}
