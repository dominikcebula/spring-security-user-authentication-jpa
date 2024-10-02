package com.dominikcebula.spring.security.user.authentication.activationlink.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ActivationTokenFactory {
    public String create() {
        return UUID.randomUUID().toString();
    }
}
