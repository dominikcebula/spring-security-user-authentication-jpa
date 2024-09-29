package com.dominikcebula.spring.security.user.authentication.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.dominikcebula.spring.security.user.authentication.login.LoginFailureHandler.AuthFailedError.*;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);

        handleAuthenticationException(request, exception);
    }

    private void handleAuthenticationException(HttpServletRequest request, AuthenticationException exception) {
        switch (exception) {
            case BadCredentialsException e -> setAuthFailedErrorAttribute(request, INVALID_USERNAME_OR_PASSWORD);
            case DisabledException e -> setAuthFailedErrorAttribute(request, ACCOUNT_NOT_ACTIVATED);
            default -> setAuthFailedErrorAttribute(request, UNKNOWN_LOGIN_ERROR);
        }
    }

    private void setAuthFailedErrorAttribute(HttpServletRequest request, AuthFailedError authFailedError) {
        request.getSession().setAttribute("AUTH_FAILED_ERROR", authFailedError);
    }

    enum AuthFailedError {
        INVALID_USERNAME_OR_PASSWORD,
        ACCOUNT_NOT_ACTIVATED,
        UNKNOWN_LOGIN_ERROR
    }
}
