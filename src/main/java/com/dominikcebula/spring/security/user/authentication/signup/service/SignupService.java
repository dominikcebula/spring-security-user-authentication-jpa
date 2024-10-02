package com.dominikcebula.spring.security.user.authentication.signup.service;

import com.dominikcebula.spring.security.user.authentication.signup.dto.SignupData;
import com.dominikcebula.spring.security.user.authentication.users.User;
import com.dominikcebula.spring.security.user.authentication.users.UserRepository;
import com.dominikcebula.spring.security.user.authentication.users.event.OnUserRegistrationCompletedEvent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.dominikcebula.spring.security.user.authentication.signup.service.SignupService.UserRegistrationResult.USER_ALREADY_EXISTS;
import static com.dominikcebula.spring.security.user.authentication.signup.service.SignupService.UserRegistrationResult.USER_REGISTERED_SUCCESSFULLY;
import static com.dominikcebula.spring.security.user.authentication.spring.data.DataIntegrityViolationExceptionUtil.isConstraintViolation;

@Service
@Validated
public class SignupService {
    private static final String ROLE_USER = "ROLE_USER";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public UserRegistrationResult registerUser(@Valid SignupData signupData) {
        User user = new User(signupData.getEmail(), passwordEncoder.encode(signupData.getPassword()), ROLE_USER, false);

        try {
            userRepository.save(user);

            eventPublisher.publishEvent(new OnUserRegistrationCompletedEvent(user));

            return USER_REGISTERED_SUCCESSFULLY;
        } catch (DataIntegrityViolationException e) {
            return handleDataIntegrityViolationException(e);
        }
    }

    private UserRegistrationResult handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        if (isConstraintViolation("users_username_key", e))
            return USER_ALREADY_EXISTS;
        else
            throw e;
    }

    public enum UserRegistrationResult {
        USER_REGISTERED_SUCCESSFULLY,
        USER_ALREADY_EXISTS,
        USER_DATA_INVALID
    }
}
