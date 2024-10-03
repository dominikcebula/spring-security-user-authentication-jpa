package com.dominikcebula.spring.security.user.authentication.users.service;

import com.dominikcebula.spring.security.user.authentication.users.db.User;
import com.dominikcebula.spring.security.user.authentication.users.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);

        return user.map(UserDetailsAdapter::new)
                .orElseThrow(() -> new UsernameNotFoundException("Unable to find username %s".formatted(username)));
    }
}
