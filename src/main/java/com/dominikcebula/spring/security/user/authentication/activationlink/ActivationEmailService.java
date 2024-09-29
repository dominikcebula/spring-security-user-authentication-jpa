package com.dominikcebula.spring.security.user.authentication.activationlink;

import com.dominikcebula.spring.security.user.authentication.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.dominikcebula.spring.security.user.authentication.activationlink.ActivationLinkController.ENDPOINT_ACTIVATE;

@Service
public class ActivationEmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendActivationEmail(User user, ActivationLink activationLink) {
        String activationUrl = getActivationUrl(activationLink);

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

    private String getActivationUrl(ActivationLink activationLink) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUri()
                .resolve(ENDPOINT_ACTIVATE) + "?token=" + activationLink.getToken();
    }
}
