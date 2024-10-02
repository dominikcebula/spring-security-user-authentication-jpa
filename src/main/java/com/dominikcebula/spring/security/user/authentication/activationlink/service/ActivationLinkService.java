package com.dominikcebula.spring.security.user.authentication.activationlink.service;

import com.dominikcebula.spring.security.user.authentication.activationlink.db.ActivationLink;
import com.dominikcebula.spring.security.user.authentication.activationlink.db.ActivationLinkRepository;
import com.dominikcebula.spring.security.user.authentication.users.User;
import com.dominikcebula.spring.security.user.authentication.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

import static com.dominikcebula.spring.security.user.authentication.activationlink.service.ActivationLinkService.ActivationResult.*;
import static com.dominikcebula.spring.security.user.authentication.activationlink.service.ActivationLinkService.TokenRegenerationResult.TOKEN_MISSING;
import static com.dominikcebula.spring.security.user.authentication.activationlink.service.ActivationLinkService.TokenRegenerationResult.TOKEN_REGENERATED;

@Component
public class ActivationLinkService {
    private static final int ACTIVATION_LINK_EXPIRY_DATE_DAYS = 1;

    @Autowired
    private ActivationTokenFactory activationTokenFactory;
    @Autowired
    private ActivationLinkRepository activationLinkRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivationEmailService activationEmailService;

    @Transactional
    public ActivationResult activateAccount(String token) {
        Optional<ActivationLink> activationLink = activationLinkRepository.findByToken(token);

        if (activationLink.isEmpty())
            return ACTIVATION_TOKEN_MISSING;

        if (isExpired(activationLink.get()))
            return ACTIVATION_TOKEN_EXPIRED;

        User user = activationLink.get().getUser();
        user.setEnabled(true);

        userRepository.save(user);
        activationLinkRepository.delete(activationLink.get());

        return ACCOUNT_ACTIVATED;
    }

    @Transactional
    public void createAndSendActivationLink(User user) {
        ActivationLink activationLink = createStoredActivationLink(user);

        activationEmailService.sendActivationEmail(user, activationLink);
    }

    public TokenRegenerationResult regenerateExpiredActivationToken(String expiredToken) {
        Optional<ActivationLink> expiredActivationLink = activationLinkRepository.findByToken(expiredToken);

        if (expiredActivationLink.isEmpty())
            return TOKEN_MISSING;

        User user = expiredActivationLink.get().getUser();

        regenerateAndSendActivationLink(expiredActivationLink.get(), user);

        return TOKEN_REGENERATED;
    }

    public Optional<String> getActivationTokenForAccount(String username) {
        return activationLinkRepository.findByUsername(username)
                .map(ActivationLink::getToken);
    }

    private boolean isExpired(ActivationLink activationLink) {
        return ZonedDateTime.now().isAfter(activationLink.getExpiryDate());
    }

    private ActivationLink createStoredActivationLink(User user) {
        ActivationLink activationLink = createActivationLink(user);

        activationLinkRepository.save(activationLink);

        return activationLink;
    }

    private ActivationLink createActivationLink(User user) {
        String activationToken = activationTokenFactory.create();
        ZonedDateTime expiryDate = ZonedDateTime.now().plusDays(ACTIVATION_LINK_EXPIRY_DATE_DAYS);

        return new ActivationLink(user, activationToken, expiryDate);
    }

    @Transactional
    private void regenerateAndSendActivationLink(ActivationLink expiredActivationLink, User user) {
        deleteExpiredActivationLink(expiredActivationLink);
        createAndSendActivationLink(user);
    }

    private void deleteExpiredActivationLink(ActivationLink expiredActivationLink) {
        activationLinkRepository.delete(expiredActivationLink);
    }

    public enum ActivationResult {
        ACCOUNT_ACTIVATED,
        ACTIVATION_TOKEN_MISSING,
        ACTIVATION_TOKEN_EXPIRED
    }

    public enum TokenRegenerationResult {
        TOKEN_REGENERATED,
        TOKEN_MISSING
    }
}
