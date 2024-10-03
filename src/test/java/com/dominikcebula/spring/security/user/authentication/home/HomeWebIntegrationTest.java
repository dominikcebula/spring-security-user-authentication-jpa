package com.dominikcebula.spring.security.user.authentication.home;

import com.dominikcebula.spring.security.user.authentication.spring.config.WebClientFactory;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class HomeWebIntegrationTest {
    @Autowired
    private WebClientFactory webClientFactory;

    @ParameterizedTest
    @ValueSource(strings = {"", "/", "/home"})
    void shouldDisplayDefaultHelloPage(String path) throws IOException {
        try (WebClient webClient = webClientFactory.getWebClient()) {
            HtmlPage page = webClient.getPage(webClientFactory.getLocalUrl(path));

            assertThat(page.getBody().asNormalizedText())
                    .contains("Authentication Status")
                    .contains("Currently, you are not authenticated.")
                    .contains("Use Login and Sign-up link in the header of this page to get started.");
        }
    }
}
