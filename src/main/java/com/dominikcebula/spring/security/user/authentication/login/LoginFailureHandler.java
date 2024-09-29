package com.dominikcebula.spring.security.user.authentication.login;

import com.dominikcebula.spring.security.user.authentication.activationlink.ActivationLinkService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import static com.dominikcebula.spring.security.user.authentication.login.LoginFailureHandler.AuthFailedError.*;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private ActivationLinkService activationLinkService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);

        handleAuthenticationException(request, exception);
    }

    private void handleAuthenticationException(HttpServletRequest request, AuthenticationException exception) {
        switch (exception) {
            case BadCredentialsException e -> handleInvalidUsernameOrPassword(request);
            case DisabledException e -> handleAccountNotActivatedYet(request);
            default -> handleUnknownLoginError(request);
        }
    }

    private void handleInvalidUsernameOrPassword(HttpServletRequest request) {
        setAuthFailedErrorAttribute(request, INVALID_USERNAME_OR_PASSWORD);
    }

    private void handleAccountNotActivatedYet(HttpServletRequest request) {
        setAuthFailedErrorAttribute(request, ACCOUNT_NOT_ACTIVATED);
        setActivationToken(request);
    }

    private void handleUnknownLoginError(HttpServletRequest request) {
        setAuthFailedErrorAttribute(request, UNKNOWN_LOGIN_ERROR);
    }

    private void setActivationToken(HttpServletRequest request) {
        String username = request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);

        Optional<String> activationTokenForAccount = activationLinkService.getActivationTokenForAccount(username);

        setActivationTokenAttribute(request, activationTokenForAccount);
    }

    private void setAuthFailedErrorAttribute(HttpServletRequest request, AuthFailedError authFailedError) {
        setAttribute(request, "AUTH_FAILED_ERROR", authFailedError);
    }

    private void setActivationTokenAttribute(HttpServletRequest request, Optional<String> activationTokenForAccount) {
        setAttribute(request, "ACTIVATION_TOKEN", activationTokenForAccount);
    }

    private <T> void setAttribute(HttpServletRequest request, String name, T value) {
        request.getSession().setAttribute(name, value);
    }

    enum AuthFailedError {
        INVALID_USERNAME_OR_PASSWORD,
        ACCOUNT_NOT_ACTIVATED,
        UNKNOWN_LOGIN_ERROR
    }
}
