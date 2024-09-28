package com.dominikcebula.spring.security.user.authentication.activationlink;

import com.dominikcebula.spring.security.user.authentication.users.User;
import com.dominikcebula.spring.security.user.authentication.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.ZonedDateTime;
import java.util.Optional;

import static com.dominikcebula.spring.security.user.authentication.activationlink.ActivationLinkController.ENDPOINT_ACTIVATE;
import static com.dominikcebula.spring.security.user.authentication.activationlink.ActivationLinkService.ActivationResult.*;

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
    private JavaMailSender mailSender;

    public ActivationResult activateAccount(String token) {
        Optional<ActivationLink> activationLink = activationLinkRepository.findByToken(token);

        if (activationLink.isEmpty())
            return ACTIVATION_TOKEN_MISSING;

        if (isExpired(activationLink.get()))
            return ACTIVATION_TOKEN_EXPIRED;

        User user = activationLink.get().getUser();
        user.setEnabled(true);

        userRepository.save(user);

        return ACCOUNT_ACTIVATED;
    }

    public void createAndSendActivationLink(User user) {
        String activationToken = createStoredActivationToken(user);

        sendActivationEmail(user, activationToken);
    }

    private boolean isExpired(ActivationLink activationLink) {
        return activationLink.getExpiryDate().isAfter(ZonedDateTime.now());
    }

    private String createStoredActivationToken(User user) {
        String activationToken = activationTokenFactory.create();
        ZonedDateTime expiryDate = ZonedDateTime.now().plusDays(ACTIVATION_LINK_EXPIRY_DATE_DAYS);

        ActivationLink activationLink = new ActivationLink(user, activationToken, expiryDate);

        activationLinkRepository.save(activationLink);

        return activationToken;
    }

    private void sendActivationEmail(User user, String activationToken) {
        String activationUrl = getActivationUrl(activationToken);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getUsername());
        email.setSubject("Confirm Your Email to Activate Your Account");
        email.setText(
                """
                        To complete your registration and activate your account,
                        please confirm your email by clicking the link below:
                        %s
                        """.formatted(activationUrl)
        );
        mailSender.send(email);
    }

    private String getActivationUrl(String activationToken) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUri()
                .resolve(ENDPOINT_ACTIVATE) + "?token=" + activationToken;
    }

    enum ActivationResult {
        ACCOUNT_ACTIVATED,
        ACTIVATION_TOKEN_MISSING,
        ACTIVATION_TOKEN_EXPIRED
    }
}
