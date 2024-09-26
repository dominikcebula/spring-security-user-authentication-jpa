package com.dominikcebula.spring.security.user.authentication.users;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);
}
