package com.dominikcebula.spring.security.user.authentication.signup.web;

import com.dominikcebula.spring.security.user.authentication.spring.config.WebClientFactory;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class SignupControllerWebIntegrationTest {
    @Autowired
    private WebClientFactory webClientFactory;

    @Test
    void shouldDisplayDefaultSignupPage() throws IOException {
        try (WebClient webClient = webClientFactory.getWebClient()) {
            HtmlPage page = webClient.getPage(webClientFactory.getLocalUrl("/signup"));

            assertThat(page.getBody().asNormalizedText())
                    .isNotBlank();
        }
    }
}