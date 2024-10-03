package com.dominikcebula.spring.security.user.authentication.users.service;

import com.dominikcebula.spring.security.user.authentication.users.db.User;
import com.dominikcebula.spring.security.user.authentication.users.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    private static final String ROLE_USER = "ROLE_USER";

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public User registerUser(String email, String password, boolean enabled) {
        User user = new User(email, passwordEncoder.encode(password), ROLE_USER, enabled);
        return userRepository.save(user);
    }
}
