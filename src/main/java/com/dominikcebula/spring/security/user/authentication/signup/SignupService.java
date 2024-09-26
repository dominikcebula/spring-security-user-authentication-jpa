package com.dominikcebula.spring.security.user.authentication.signup;

import com.dominikcebula.spring.security.user.authentication.users.User;
import com.dominikcebula.spring.security.user.authentication.users.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class SignupService {
    private static final String ROLE_USER = "ROLE_USER";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(@Valid SignupData signupData) {
        User user = new User(signupData.getEmail(), passwordEncoder.encode(signupData.getPassword()), ROLE_USER, true);

        userRepository.save(user);
    }
}
