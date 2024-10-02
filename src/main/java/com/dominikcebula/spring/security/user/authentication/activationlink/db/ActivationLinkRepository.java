package com.dominikcebula.spring.security.user.authentication.activationlink.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ActivationLinkRepository extends ListCrudRepository<ActivationLink, Integer> {
    Optional<ActivationLink> findByToken(String token);

    @Query("""
            select a from ActivationLink a
            join User u on a.user.id=u.id
            where u.username=?1
            """)
    Optional<ActivationLink> findByUsername(String username);
}
