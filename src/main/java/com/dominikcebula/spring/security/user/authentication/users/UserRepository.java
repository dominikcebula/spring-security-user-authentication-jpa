package com.dominikcebula.spring.security.user.authentication.users;

import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<User, Integer> {
}
