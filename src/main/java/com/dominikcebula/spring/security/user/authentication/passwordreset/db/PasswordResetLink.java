package com.dominikcebula.spring.security.user.authentication.passwordreset.db;

import com.dominikcebula.spring.security.user.authentication.users.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "password_reset_links")
@Data
@NoArgsConstructor
public class PasswordResetLink {
    @Id
    @Column(name = "link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String token;

    private ZonedDateTime expiryDate;

    public PasswordResetLink(User user, String token, ZonedDateTime expiryDate) {
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
