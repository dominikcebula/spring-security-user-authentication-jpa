package com.dominikcebula.spring.security.user.authentication.passwordreset.service;

import com.dominikcebula.spring.security.user.authentication.passwordreset.db.PasswordResetLink;
import com.dominikcebula.spring.security.user.authentication.passwordreset.db.PasswordResetLinkRepository;
import com.dominikcebula.spring.security.user.authentication.passwordreset.dto.PasswordResetData;
import com.dominikcebula.spring.security.user.authentication.passwordreset.dto.PasswordResetEmailData;
import com.dominikcebula.spring.security.user.authentication.users.User;
import com.dominikcebula.spring.security.user.authentication.users.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.ZonedDateTime;
import java.util.Optional;

import static com.dominikcebula.spring.security.user.authentication.passwordreset.service.PasswordResetEmailService.PasswordResetEmailSendResult;
import static com.dominikcebula.spring.security.user.authentication.passwordreset.service.PasswordResetService.PasswordResetResult.*;
import static com.dominikcebula.spring.security.user.authentication.passwordreset.service.PasswordResetService.SendPasswordResetLinkResult.*;

@Service
@Validated
public class PasswordResetService {
    private static final int PASSWORD_RESET_LINK_EXPIRY_DATE_DAYS = 1;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenFactory passwordResetTokenFactory;
    @Autowired
    private PasswordResetLinkRepository passwordResetLinkRepository;
    @Autowired
    private PasswordResetEmailService passwordResetEmailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public SendPasswordResetLinkResult createAndSendPasswordResetLink(@Valid PasswordResetEmailData passwordResetEmailData) {
        Optional<User> user = userRepository.findUserByUsername(passwordResetEmailData.getEmail());

        if (user.isEmpty())
            return PASSWORD_RESET_USER_DOES_NOT_EXISTS_PRETENDED_SUCCESS;

        PasswordResetLink passwordResetLink = createStoredPasswordResetLink(user.get());
        PasswordResetEmailSendResult passwordResetEmailSendResult = passwordResetEmailService.trySendPasswordResetEmail(user.get(), passwordResetLink);

        return switch (passwordResetEmailSendResult) {
            case PASSWORD_RESET_EMAIL_SENT_SUCCESSFULLY -> PASSWORD_RESET_LINK_SENT;
            case PASSWORD_RESET_EMAIL_SENDING_ERROR -> PASSWORD_RESET_LINK_SENDING_ERROR;
        };
    }

    @Transactional
    public PasswordResetResult resetPasswordUsingToken(@Valid PasswordResetData passwordResetData) {
        Optional<PasswordResetLink> passwordResetLink = passwordResetLinkRepository.findByToken(passwordResetData.getToken());

        if (passwordResetLink.isEmpty())
            return PASSWORD_RESET_TOKEN_NOT_FOUND;

        if (isExpired(passwordResetLink.get()))
            return PASSWORD_RESET_TOKEN_EXPIRED;

        User user = passwordResetLink.get().getUser();
        user.setPassword(passwordEncoder.encode(passwordResetData.getPassword()));

        userRepository.save(user);
        passwordResetLinkRepository.delete(passwordResetLink.get());

        return PASSWORD_RESET_SUCCESSFUL;
    }

    private PasswordResetLink createStoredPasswordResetLink(User user) {
        PasswordResetLink passwordResetLink = createPasswordResetLink(user);

        passwordResetLinkRepository.deleteByUserId(user.getId());
        passwordResetLinkRepository.save(passwordResetLink);

        return passwordResetLink;
    }

    private PasswordResetLink createPasswordResetLink(User user) {
        String passwordResetToken = passwordResetTokenFactory.create();
        ZonedDateTime expiryDate = ZonedDateTime.now().plusDays(PASSWORD_RESET_LINK_EXPIRY_DATE_DAYS);

        return new PasswordResetLink(user, passwordResetToken, expiryDate);
    }

    private boolean isExpired(PasswordResetLink passwordResetLink) {
        return ZonedDateTime.now().isAfter(passwordResetLink.getExpiryDate());
    }

    public enum SendPasswordResetLinkResult {
        PASSWORD_RESET_DATA_INVALID(false),
        PASSWORD_RESET_USER_DOES_NOT_EXISTS_PRETENDED_SUCCESS(true),
        PASSWORD_RESET_LINK_SENDING_ERROR(false),
        PASSWORD_RESET_LINK_SENT(true);

        private final boolean isSuccessful;

        SendPasswordResetLinkResult(boolean isSuccessful) {
            this.isSuccessful = isSuccessful;
        }

        public boolean isSuccessful() {
            return isSuccessful;
        }
    }

    public enum PasswordResetResult {
        PASSWORD_RESET_STATUS_UNKNOWN(false),
        PASSWORD_RESET_DATA_INVALID(false),
        PASSWORD_RESET_TOKEN_NOT_FOUND(false),
        PASSWORD_RESET_TOKEN_EXPIRED(false),
        PASSWORD_RESET_SUCCESSFUL(true);

        private final boolean isSuccessful;

        PasswordResetResult(boolean isSuccessful) {
            this.isSuccessful = isSuccessful;
        }

        public boolean isSuccessful() {
            return isSuccessful;
        }
    }
}
