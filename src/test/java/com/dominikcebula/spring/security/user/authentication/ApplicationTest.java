package com.dominikcebula.spring.security.user.authentication;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ApplicationTest {

    @Value(value = "${local.server.port}")
    private int port;

    @ParameterizedTest
    @ValueSource(strings = {"", "/", "/home"})
    void shouldDisplayDefaultHelloPage(String path) throws IOException {
        try (WebClient webClient = getWebClient()) {
            HtmlPage page = webClient.getPage(getLocalUrl(path));

            assertThat(page.getBody().asNormalizedText())
                    .isNotBlank();
        }
    }

    private WebClient getWebClient() {
        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        return webClient;
    }

    private String getLocalUrl(String path) {
        return "http://localhost:%d%s".formatted(port, path);
    }
}
