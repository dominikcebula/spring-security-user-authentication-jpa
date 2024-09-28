package com.dominikcebula.spring.security.user.authentication.activationlink;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ActivationLinkRepository extends ListCrudRepository<ActivationLink, Integer> {
    Optional<ActivationLink> findByToken(String token);
}
