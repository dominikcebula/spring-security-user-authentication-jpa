package com.dominikcebula.spring.security.user.authentication.passwordreset.service;

import com.dominikcebula.spring.security.user.authentication.common.url.ServerUrlResolver;
import com.dominikcebula.spring.security.user.authentication.passwordreset.db.PasswordResetLink;
import com.dominikcebula.spring.security.user.authentication.users.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.dominikcebula.spring.security.user.authentication.passwordreset.service.PasswordResetEmailService.PasswordResetEmailSendResult.PASSWORD_RESET_EMAIL_SENDING_ERROR;
import static com.dominikcebula.spring.security.user.authentication.passwordreset.service.PasswordResetEmailService.PasswordResetEmailSendResult.PASSWORD_RESET_EMAIL_SENT_SUCCESSFULLY;
import static com.dominikcebula.spring.security.user.authentication.passwordreset.web.PasswordResetController.ENDPOINT_PASSWORD_RESET_USING_TOKEN;

@Service
@Slf4j
public class PasswordResetEmailService {
    @Autowired
    private JavaMailSender mailSender;

    public PasswordResetEmailSendResult trySendPasswordResetEmail(User user, PasswordResetLink passwordResetLink) {
        try {
            sendPasswordResetEmail(user, passwordResetLink);

            return PASSWORD_RESET_EMAIL_SENT_SUCCESSFULLY;
        } catch (Exception e) {
            log.warn("Error occurred while sending password reset e-mail", e);
            return PASSWORD_RESET_EMAIL_SENDING_ERROR;
        }
    }

    private void sendPasswordResetEmail(User user, PasswordResetLink passwordResetLink) {
        String passwordResetUrl = getPasswordResetUrl(passwordResetLink);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getUsername());
        email.setSubject("Password reset");
        email.setText(
                """
                        To reset password for your account, please follow the link below:
                        %s
                        """.formatted(passwordResetUrl)
        );
        mailSender.send(email);
    }

    private String getPasswordResetUrl(PasswordResetLink passwordResetLink) {
        return ServerUrlResolver
                .getServerUrl()
                .resolve(ENDPOINT_PASSWORD_RESET_USING_TOKEN) + "?token=" + passwordResetLink.getToken();
    }

    enum PasswordResetEmailSendResult {
        PASSWORD_RESET_EMAIL_SENT_SUCCESSFULLY,
        PASSWORD_RESET_EMAIL_SENDING_ERROR
    }
}
