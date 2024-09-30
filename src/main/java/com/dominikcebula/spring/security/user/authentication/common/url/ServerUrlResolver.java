package com.dominikcebula.spring.security.user.authentication.common.url;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class ServerUrlResolver {
    private ServerUrlResolver() {
    }

    public static URI getServerUrl() {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUri();
    }
}
