package com.dominikcebula.spring.security.user.authentication.login;

import com.dominikcebula.spring.security.user.authentication.spring.config.WebClientFactory;
import org.htmlunit.WebClient;
import org.htmlunit.html.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class LoginControllerWebIntegrationTest {
    @Autowired
    private WebClientFactory webClientFactory;

    @Test
    void shouldDisplayDefaultLoginPage() throws IOException {
        try (WebClient webClient = webClientFactory.getWebClient()) {
            HtmlPage page = webClient.getPage(webClientFactory.getLocalUrl("/login"));

            HtmlForm loginForm = findLoginForm(page);
            assertNotNull(loginForm);

            HtmlEmailInput username = findUsernameField(loginForm);
            assertNotNull(username);

            HtmlPasswordInput password = findPasswordField(loginForm);
            assertNotNull(password);

            HtmlCheckBoxInput rememberMe = findRememberMeCheckbox(loginForm);
            assertNotNull(rememberMe);

            HtmlAnchor passwordResetLink = findPasswordResetLink(loginForm);
            assertNotNull(passwordResetLink);

            HtmlButton loginButton = findLoginButton(loginForm);
            assertNotNull(loginButton);
        }
    }

    private HtmlForm findLoginForm(HtmlPage page) {
        return page.getFormByName("login");
    }

    private HtmlEmailInput findUsernameField(HtmlForm loginForm) {
        return loginForm.getInputByName("username");
    }

    private HtmlPasswordInput findPasswordField(HtmlForm loginForm) {
        return loginForm.getInputByName("password");
    }

    private HtmlCheckBoxInput findRememberMeCheckbox(HtmlForm loginForm) {
        return loginForm.getInputByName("remember-me");
    }

    private HtmlAnchor findPasswordResetLink(HtmlForm loginForm) {
        return (HtmlAnchor) loginForm.getByXPath("//a[@id='passwordResetLink']").getFirst();
    }

    private HtmlButton findLoginButton(HtmlForm loginForm) {
        return loginForm.getButtonByName("login");
    }
}