package com.dominikcebula.spring.security.user.authentication.signup;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class SignupService {
    public void registerUser(@Valid SignupData signupData) {

    }
}
