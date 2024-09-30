package com.dominikcebula.spring.security.user.authentication.passwordreset;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface PasswordResetLinkRepository extends ListCrudRepository<PasswordResetLink, Integer> {
    Optional<PasswordResetLink> findByToken(String token);

    @Query("""
            select p from PasswordResetLink p
            join User u on p.user.id=u.id
            where u.username=?1
            """)
    Optional<PasswordResetLink> findByUsername(String username);
}
