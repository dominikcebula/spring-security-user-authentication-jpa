package com.dominikcebula.spring.security.user.authentication.activationlink;

import com.dominikcebula.spring.security.user.authentication.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.dominikcebula.spring.security.user.authentication.activationlink.ActivationLinkController.ENDPOINT_ACTIVATE;

@Component
public class ActivationLinkService {
    @Autowired
    private ActivationTokenFactory activationTokenFactory;
    @Autowired
    private ActivationLinkRepository activationLinkRepository;
    @Autowired
    private JavaMailSender mailSender;

    public void activateAccount(String token) {

    }

    public void createAndSendActivationLink(User user) {
        String activationToken = createStoredActivationToken(user);

        sendActivationEmail(user, activationToken);
    }

    private String createStoredActivationToken(User user) {
        String activationToken = activationTokenFactory.create();
        ActivationLink activationLink = new ActivationLink(user, activationToken);

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
}
