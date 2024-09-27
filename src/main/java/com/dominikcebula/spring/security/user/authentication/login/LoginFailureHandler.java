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

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);

        request.getSession().setAttribute("AUTH_FAILED_MESSAGE", getErrorMessage(exception));
    }

    private String getErrorMessage(AuthenticationException exception) {
        return switch (exception) {
            case BadCredentialsException e -> "Invalid username or password.";
            case DisabledException e -> "Account was not activated yet.";
            default -> "Login error.";
        };
    }
}
