package com.dominikcebula.spring.security.user.authentication.spring.config;

import org.htmlunit.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class WebClientFactory {
    @Autowired
    private Environment environment;

    public WebClient getWebClient() {
        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        return webClient;
    }

    public String getLocalUrl(String path) {
        return "http://localhost:%d%s".formatted(getPort(), path);
    }

    private int getPort() {
        return Integer.parseInt(environment.getProperty("local.server.port"));
    }
}
